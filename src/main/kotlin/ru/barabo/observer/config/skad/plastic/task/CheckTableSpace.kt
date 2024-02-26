package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.modifyTestScript
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.*
import java.time.LocalTime
import java.time.temporal.ChronoUnit


object CheckTableSpace : SinglePerpetual {

    override fun name(): String = "Проверка Table space"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit = ChronoUnit.HOURS

    override val countTimes: Long = 6

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(6, 30), workTimeTo = LocalTime.of(23, 50) )

    override fun execute(elem: Elem): State {
        val spaces = AfinaQuery.selectCursor(CURSOR_TABLE_SPACE)

        for (space in spaces) {
            val name = space[0] as String

            val freeMb = (space[1] as Number).toInt()

            val totalMb = (space[2] as Number).toInt()

            val minimumMb = (space[3] as Number).toInt()

            val mustAddFile = (space[4] as Number).toInt()

            logger.error("TABLE_SPACE $name FREE=$freeMb TOTAL=$totalMb MIN=$minimumMb")

            if(freeMb <= minimumMb) {
                sendAlarm(name, freeMb, totalMb, mustAddFile)
            }

            if(freeMb <= mustAddFile) {
                addFileTableSpace(name)
            }
        }

        return super.execute(elem)
    }

    private fun sendAlarm(tableSpace: String, freeMb: Int, totalMb: Int, mustAddFile: Int) {

        val subject = "✖☢☠ Место в TableSpace заканчивается!!!"

        val body = """
    Внимание! \n В TableSpace $tableSpace осталось меньше $freeMb MB, всего tableSpace занимает $totalMb MB
Когда места останется меньше $mustAddFile MB, то автоматически должен добавиться новый файл данных. Но если что-то пойдет не так, 
тогда зовите DBA.

P.S. DBA все-равно придется звать, даже если автоматом файл добавится. Т.к. тестовая база с новым файлом не стартанет."""

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, cc = BaraboSmtp.AUTO,
            subject = subject, body = body, charsetSubject = "UTF-8")
    }

    private fun addFileTableSpace(tableSpace: String) {

        val nextFileName = AfinaQuery.selectValue(SELECT_NEXT_FILENAME, arrayOf(tableSpace) ) as String

        val currentFileName = AfinaQuery.selectValue(SELECT_CURRENT_FILENAME, arrayOf(tableSpace) ) as String

        val command = commandFileTableSpace(tableSpace, nextFileName)

        AfinaQuery.execute(command)

        val dataTextFile = modifyTestScript(currentFileName, nextFileName)

        val textFile = File("${Cmd.tempFolder("bak").absoluteFile}/af.sql")

        textFile.writeText(dataTextFile)

        sendInfoFileAdded(tableSpace, nextFileName, command, textFile)
    }

    private fun sendInfoFileAdded(tableSpace: String, fileName: String, command: String, textFile: File) {

        val subject = "Добавлен новый файл в tableSpace $tableSpace"

        val body = "$subject\nимя файла: $fileName\nкоманда добавления:$command\nТакже модифицирован файл бэкапа (во вложении) ${textFile.name}"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, cc = BaraboSmtp.AUTO,
            subject = subject, body = body, charsetSubject = "UTF-8", attachments = arrayOf(textFile)
        )
    }

    private fun commandFileTableSpace(tableSpace: String, fileName: String): String =
        "alter tablespace $tableSpace add datafile '$fileName' size 104M autoextend on next 1m"
}

private const val SELECT_NEXT_FILENAME = "select od.PTKB_PRECEPT.getNextFullFileName( ? ) from dual"

private const val SELECT_CURRENT_FILENAME = "select od.PTKB_PRECEPT.getCurrentFullFileName( ? ) from dual"


private const val CURSOR_TABLE_SPACE = "{ ? = call od.PTKB_PRECEPT.getTableSpaceSize }"

private val logger = LoggerFactory.getLogger(CheckTableSpace::class.java)

fun setRight() {

    logger.error("${System.getProperty("user.name")}")

    val file: Path = Paths.get("\\\\192.168.0.35\\work1\\Dep_Plan")
    val aclAttr: AclFileAttributeView = Files.getFileAttributeView(file, AclFileAttributeView::class.java)
    //println(aclAttr.owner)
    logger.error("${aclAttr.owner}")
    for (aclEntry in aclAttr.acl) {
        logger.error("$aclEntry")
        //System.out.println(aclEntry)
    }
    //println()

    val upls: UserPrincipalLookupService = file.fileSystem.userPrincipalLookupService
    val user: UserPrincipal = upls.lookupPrincipalByName("PTKB\\nazarov") //System.getProperty("user.name")
    val builder: AclEntry.Builder = AclEntry.newBuilder()
    builder
        .setType(AclEntryType.ALLOW)
        .setPrincipal(user)
        .setPermissions(
        //EnumSet.of(
            AclEntryPermission.READ_DATA, AclEntryPermission.EXECUTE,
            AclEntryPermission.READ_ACL, AclEntryPermission.READ_ATTRIBUTES, AclEntryPermission.READ_NAMED_ATTRS //,
            //AclEntryPermission.WRITE_ACL, AclEntryPermission.DELETE
        //)
        )
        .setFlags(AclEntryFlag.FILE_INHERIT, AclEntryFlag.DIRECTORY_INHERIT)
        .build()

    //builder.setPrincipal(user)
    //builder.setType(AclEntryType.ALLOW)
    //aclAttr.acl = Collections.singletonList(builder.build())
}
