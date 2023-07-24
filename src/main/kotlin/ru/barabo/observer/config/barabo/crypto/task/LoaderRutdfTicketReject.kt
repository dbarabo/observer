package ru.barabo.observer.config.barabo.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.nio.charset.Charset

object LoaderRutdfTicketReject {

    fun loadTicket(ticketFile: File) {

        val lastSentRutDfFile = findLastSentRutDfFile(ticketFile.nameWithoutExtension)

        var state = StateFind.FIND_ERROR

        var errorBuffer = ""

        var event = ""

        var uid = ""

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
                        event = findEvent(line)

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
                            saveError(lastSentRutDfFile, uid, event, errorBuffer)
                            StateFind.FIND_ERROR
                        }
                    } else if(line.indexOf("B45_APPLICATION\t") == 0) {

                        val tags = line.split('\t')
                        if(tags.size > 4) {
                            uid = tags[4].trim()
                            saveError(lastSentRutDfFile, uid, event, errorBuffer)
                            StateFind.FIND_ERROR
                        }
                    }
                }

                StateFind.FIND_GUARANTOR -> {
                    if(line.indexOf("B24_GUARANTOR\t") == 0) {

                        val guarantorUid = guarantorUid(line)

                        state = StateFind.FIND_ERROR

                        saveError(lastSentRutDfFile, uid, event, errorBuffer, guarantorUid)
                    } else {
                        saveError(lastSentRutDfFile, uid, event, errorBuffer)
                        state = StateFind.FIND_ERROR
                    }
                }
            }
        }
    }

    private fun findEvent(line: String): String {
        val params = line.split('\t')

        return params[2]
    }

    private fun guarantorUid(line: String): String {
        val params = line.split('\t')

        return params[2]
    }

    private fun findLastSentRutDfFile(fileNameTicketNoExt: String): Number {

        val withoutReject = fileNameTicketNoExt.substringBefore("_reject")

        return AfinaQuery.selectValue(SELECT_LAST_SENT_RUTDF_FILE, arrayOf(withoutReject)) as Number
    }

    private fun saveError(rutDfFile: Number, uid: String, event: String, error: String, guarantorUid: String = "") {

        AfinaQuery.execute(EXEC_SAVE_ERROR, arrayOf(rutDfFile, uid, event, error, guarantorUid))
    }
}

private const val SELECT_LAST_SENT_RUTDF_FILE = "select od.PTKB_RUTDF.getLastRutDf(?) from dual"

private const val EXEC_SAVE_ERROR = "{ call od.PTKB_RUTDF.saveError(?, ?, ?, ?, ?) }"

private enum class StateFind {
    FIND_ERROR,
    FIND_GROUP_HEADER,
    FIND_UID,
    FIND_GUARANTOR
}