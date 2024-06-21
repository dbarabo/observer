package ru.barabo.observer.config.barabo.crypto.task

import org.slf4j.LoggerFactory
import ru.barabo.db.Query
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.ibank.task.toDate
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.fns.ens.task.dateFolder
import ru.barabo.observer.config.skad.anywork.task.xNbki
import ru.barabo.observer.config.task.finder.isFind
import java.io.File
import java.nio.charset.Charset
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object LoaderRutdfTicketReject {

    private val logger = LoggerFactory.getLogger(LoaderRutdfTicketReject::class.java)

    fun loadTicket(ticketFile: File) {

        val lastSentRutDfFile = findLastSentRutDfFile(ticketFile.nameWithoutExtension)

        var state = StateFind.FIND_ERROR

        var errorBuffer = ""

        var event = ""

        var uid = ""

        var dateEvent: LocalDate? = null

        for(line in ticketFile.readLines(Charset.forName("CP1251"))) {

            when(state) {

                StateFind.FIND_ERROR -> {
                    if(line.indexOf("ERROR\t") == 0) {
                        errorBuffer = line

                        state = StateFind.FIND_GROUP_HEADER
                    }
                }

                StateFind.FIND_GROUP_HEADER -> {
                    if(line.indexOf("0_GROUPHEADER\t") == 0) {

                        val (ev,  dt) = findEvent(line)
                        event = ev
                        dateEvent = dt

                        state = StateFind.FIND_UID
                    } else {
                        errorBuffer += line
                    }
                }

                StateFind.FIND_UID -> {
                    if((line.indexOf("C17_UID\t") == 0) ||
                        (line.indexOf("B10_UID\t") == 0)) {

                        uid = line.substringAfter('\t').trim()

                        state = if(event == "2.4") {
                            StateFind.FIND_GUARANTOR
                        } else {
                            saveError(lastSentRutDfFile, uid, event, errorBuffer, eventDate = dateEvent)
                            StateFind.FIND_ERROR
                        }

                    } else if((line.indexOf("B45_APPLICATION\t") == 0) ||
                        (line.indexOf("C55_APPLICATION\t") == 0)
                    ) {

                        val tags = line.split('\t')
                        if(tags.size > 4) {
                            uid = tags[4].trim()
                            saveError(lastSentRutDfFile, uid, event, errorBuffer, eventDate = dateEvent)
                            state = StateFind.FIND_ERROR
                        }
                    }
                }

                StateFind.FIND_GUARANTOR -> {
                    if(line.indexOf("B24_GUARANTOR\t") == 0) {

                        val guarantorUid = guarantorUid(line)

                        state = StateFind.FIND_ERROR

                        saveError(lastSentRutDfFile, uid, event, errorBuffer, guarantorUid, dateEvent)
                    } else {
                        saveError(lastSentRutDfFile, uid, event, errorBuffer, eventDate = dateEvent)
                        state = StateFind.FIND_ERROR
                    }
                }
            }
        }
    }

    private fun findEvent(line: String): Pair<String, LocalDate> {
        val params = line.split('\t')

        return Pair(params[2], params[5].toDateByPoint())
    }

    private fun guarantorUid(line: String): String {
        val params = line.split('\t')

        return params[2]
    }

    private fun findLastSentRutDfFile(fileNameTicketNoExt: String): Number {

        val withoutReject = fileNameTicketNoExt.substringBefore("_reject")

        return AfinaQuery.selectValue(SELECT_LAST_SENT_RUTDF_FILE, arrayOf(withoutReject)) as Number
    }

    private fun saveError(rutDfFile: Number, uid: String, event: String, error: String,
                          guarantorUid: String = "", eventDate: LocalDate?) {

        val dateEvent = eventDate?.toTimestamp() ?: LocalDateTime::class.javaObjectType

        val params: Array<Any?> = arrayOf(rutDfFile, uid, event, error, guarantorUid, dateEvent)

        AfinaQuery.execute(EXEC_SAVE_ERROR, params)
    }

    fun checkTradeByPath(startDate: LocalDate, endExlusive: LocalDate = LocalDate.now()) {

        val maskFile = "K301BB000001_\\d{8}_\\d{6}"

        val patterFile = Pattern.compile(maskFile, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)


        var loopDate = startDate

        var count = 0

        while (loopDate < endExlusive) {

            val directory = File("${xNbki()}/${loopDate.dateFolder()}/CRYPTO" )

            directory.listFiles { f ->
                !f.isDirectory && patterFile.isFind(f.name)
            }?.forEach {
                count = findTradeByFile(it, count)
            }

            loopDate = loopDate.plusDays(1)
        }
    }

    fun findTradeByFile(file: File, countIn: Int): Int {
        logger.error("FILE=${file.absoluteFile}")

        var countOut = countIn

        val lines = file.readLines(Charset.forName("cp1251"))

        var guid = ""

        for(line in lines) {

            when {
                (line.indexOf("C17_UID\t") == 0 || line.indexOf("B10_UID\t") == 0 ) ->
                    guid = line.substringAfter('\t').trim()

                (line.indexOf("C18_TRADE\t") == 0 || line.indexOf("B11_TRADE\t") == 0 ) -> {

                    val dateString = line.substringAfter ('\t')
                        .substringAfter ('\t')
                        .substring(0..9)


                    logger.error("GUID=$guid")
                    logger.error("DATE_TRADE=$dateString")

                    countOut++

                    logger.error("countOut=$countOut")

                    val loanDate = AfinaQuery.selectValue(SELECT_DATE_LOAN, arrayOf(guid)) as Timestamp

                    if(loanDate.toLocalDateTime().toLocalDate() != dateString.toDateByPoint()) {
                        logger.error("!!!!!!!!!! ERROR DATE LOAN = ${loanDate.toLocalDateTime().toLocalDate()}")
                    }


                }
            }
        }

        return countOut
    }
}

private const val SELECT_DATE_LOAN = "select od.PTKB_RUTDF.getStartDateByGuid( ? ) from dual"

fun String.toDateByPoint(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

private const val SELECT_LAST_SENT_RUTDF_FILE = "select od.PTKB_RUTDF.getLastRutDf(?) from dual"

private const val EXEC_SAVE_ERROR = "{ call od.PTKB_RUTDF.saveError(?, ?, ?, ?, ?, ?) }"

private enum class StateFind {
    FIND_ERROR,
    FIND_GROUP_HEADER,
    FIND_UID,
    FIND_GUARANTOR
}