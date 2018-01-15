package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.EsProcess
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object GetProcess550pFiles : FileFinder, FileProcessor {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","wz...005\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "550-П Получить и отправить"

    private fun folder550pIn() :String = "X:/550-П/In/${todayFolder()}"

    private fun todayFolder() :String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

    override fun processFile(file : File) {
        val arjArchives = Archive.extractFromCab(file, folder550pIn())

        val tempFolder = Cmd.tempFolder("p550")

        arjArchives?.forEach {
            val xmlCrypto = Archive.extractFromArj(it, tempFolder.absolutePath)

            Verba.unCryptoAndUnSigned(tempFolder, "*.XML")

            tempFolder.listFiles()?.filter { !it.isDirectory }?.forEach {
                it.renameTo(File("${folder550pIn()}/${it.name}"))
            }

            tempFolder.delete()

            val search = Pattern.compile("CB.*\\.XML", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

            val xmlInFiles = File(folder550pIn()).listFiles { f ->
                !f.isDirectory && search.isFind(f.name, false)
            }

            xmlInFiles?.forEach { EsProcess.process(it) }

            val xmlOutFiles = File(EsProcess.folder550pOut()).listFiles { f ->
                !f.isDirectory && f.name.indexOf(".XML", ignoreCase = true) > 0
            }

            val archiveFileName = archiveFileName()

            Archive.addToArj(archiveFileName, xmlOutFiles)

            val signFile = Verba.signByCbr(File(archiveFileName))

            SendByPtkPsdCopy.executeFile(signFile)

            sendMailFilesInfo(archiveFileName, xmlCrypto)
        }
    }

    private val SUBJECT_INFO = "Получены и обработаны файлы по 550-П"

    private fun sendMailFilesInfo(archiveFileName :String, xmlInFiles :Array<File>?) {

        val inFiles = xmlInFiles?.joinToString("\n") { "${folder550pIn()}/${it.name}" }

        val outFiles = xmlInFiles?.joinToString("\n") { "${folder550pIn()}/${EsProcess.PREFIX_TICKET}${it.name}" }

        val text = "\tПолучены файлы по 550-П\n" +
                "\tСписок файлов:\n$inFiles\n" +
                "\tФайлы успешно обработаны и созданы ответные файлы квитанций\n$outFiles\n" +
                "\tОтправлен архивный файл с ответами:$archiveFileName"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.PODFT, bcc = BaraboSmtp.CHECKER_550P,
                subject = SUBJECT_INFO, body = text)
    }

    private fun archiveFileName() :String {

        val search = Pattern.compile("ARH550P_0021_0000_${todayBlind()}_...\\.arj",
                Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val max = File(EsProcess.folder550pOut()).listFiles { f ->
            !f.isDirectory && search.matcher(f.name).find()}
                ?.map { Integer.parseInt(it.name.substring(it.name.indexOf(".") - 3, it.name.indexOf(".")))}?.max()?:0 + 1

        return "${EsProcess.folder550pOut()}/ARH550P_0021_0000_${todayBlind()}_00$max.arj"
    }

    private fun todayBlind() :String = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())
}