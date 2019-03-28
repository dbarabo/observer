package ru.barabo.observer.config.cbr.ticket.task

import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.template.file.FileProcessor
import ru.barabo.observer.crypto.Verba
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory

object Ticket311pFns: FileFinder, FileProcessor {

    private val logger = LoggerFactory.getLogger(Ticket311pFns::class.java)

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( "C:/PTK_POST/ELO/OUT","2z..._05\\.717", isModifiedTodayOnly = true))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, true, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "(311-П ФНС)"

    override fun processFile(file : File) {

        val files = Archive.extractFromCab(file, Ticket311pCbr.ticket311p(), ".*\\.arj")

        files?.forEach { Archive.extractFromArj(it, Ticket311pCbr.ticket311p()) }

        val search = Pattern.compile("S.*\\.XML", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val xmlFiles = File( Ticket311pCbr.ticket311p()).listFiles {
            f ->  !f.isDirectory && search.isFind(f.name, false)}

        xmlFiles?.forEach {
            Verba.unSignFile(it)

            loadXmlToAfina(it)
        }
    }

    private fun loadXmlToAfina(fileXml :File) {

        val head = DocumentBuilderFactory.newInstance()?.newDocumentBuilder()?.parse(fileXml)?.
                documentElement?.getElementsByTagName("Документ")?.item(0) as? Element

        saveTicket(head?.getAttribute("НомСооб"), head?.getAttribute("РезОбр"), fileXml)
    }

    private const val SELECT_ID_REGISTER = "select max(rp.id) from od.ptkb_361p_register rp where rp.NUMBER_MAIL = ?"

    private const val UPDATE_REGISTER = "update od.ptkb_361p_register rp set rp.KWIT_DATE = sysdate, " +
            "rp.KWIT_RESULT = ?, rp.KWIT_FILENAME = ?, rp.KWIT_DATA = ?, rp.STATE = 9 where rp.id = ?"

    private fun saveTicket(numberMessage: String?, resultMessage: String?, fileXml: File) {

        val number =try {
            if(numberMessage.isNullOrEmpty()) getNumberByFile(fileXml)
            else (numberMessage.trim().toLong() % 1000000).toInt()
        } catch (e: Exception) {
            logger.error("saveTicket", e)

            logger.error("numberMessage=$numberMessage")

            logger.error("fileXml=$fileXml")

            throw Exception(e.message!!)
        }

        val idRegister = AfinaQuery.selectValue(SELECT_ID_REGISTER, arrayOf(number)) as? Number

        val params :Array<Any?> = arrayOf(resultMessage, fileXml.name,
                fileXml.readText(Charset.forName("CP1251")), idRegister)

        AfinaQuery.execute(UPDATE_REGISTER, params)
    }

    private fun getNumberByFile(fileXml: File): Int = (fileXml.nameWithoutExtension
            .substringAfterLast("0000").substringBefore('_').toLong() % 1000000).toInt()
}
