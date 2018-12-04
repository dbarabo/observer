package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi.hCardOutFileToday
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi.hCardOutToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.nio.charset.Charset
import java.time.LocalTime

object IbiSendToJzdo : FileFinder, FileProcessor {

    override fun name(): String = "IBI-отправить файл в ПЦ"

    override fun config(): ConfigTask = PlasticTurnConfig

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(::hCardOutFileToday, "IBI_.*"))

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS, isDuplicateName = false,
            workTimeFrom = LocalTime.of(7, 0) )

    fun hCardOutSentTodayFolder(): File = "${hCardOutToday()}/sent".byFolderExists()

    fun hCardOutSentTodayByFolder(): String = hCardOutSentTodayFolder().absolutePath

    private val TO_JZDO_SENT = if(TaskMapper.isAfinaBase())"\\\\jzdo/c$/jzdo/files/doc/out/cs/unknown" else "c:/temp"

    fun toJzdoSent(): String = TO_JZDO_SENT

    override fun processFile(file: File) {

        val toHcardSent = File("${hCardOutSentTodayFolder()}/${file.name}")

        file.copyTo(toHcardSent, true)

        if(isExistsIbiTable(file)) {

            val toJzdo = File("$TO_JZDO_SENT/${file.name}")
            file.copyTo(toJzdo, true)
        }

        file.delete()
    }

    private const val SUBJECT_ERROR_IBI_EXISTS = "Ошибка Пластик: Попытка отправить незарегистрированный IBI-файл"

    private fun errorBody(idFile :Any?, file :File) = "IBI-файл не найден в таблице PTKB_IBI_MAIN.ID=$idFile Файл:${file.name}"

    private fun isExistsIbiTable(file :File) :Boolean {
        val idFile = file.readLines( Charset.forName("CP1251"))
                .firstOrNull{it.startsWith("RCTP01") && it.length >= 38}?.substring(20, 20 + 18)?.trim()?.toLong()

        val exists = idFile?.let { AfinaQuery.selectValue(SELECT_ID_IBI, arrayOf(idFile)) }

        if(exists == null) {
            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_ERROR_IBI_EXISTS,
                    body = errorBody(idFile, file) )
        }

        return exists != null
    }

    private const val SELECT_ID_IBI = "select id from od.PTKB_IBI_MAIN m where m.id = ?"
}