package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.crypto.task.toDateByPoint
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate

object LoaderNbkiFileSent {

    fun load(start: LocalDate) {

        val mapFiles = AfinaQuery
            .select(SELECT_FILES, arrayOf(start.toTimestamp()))
            .associate { (it[0] as Number).toLong() to (it[1] as String) }

        for(entry in mapFiles.entries) {

            val file = File(nbkiFullFileName(entry.value) )

            logger.error("key=${entry.key} file=$file")

            processFile(entry.key, file)
        }
    }

    private fun processFile(idFile: Long, file: File) {

        var ev = Event()

        for(line in file.readLines(Charset.forName("CP1251"))) {

            val tag = line.substringBefore('\t')

            when (ev.state) {
                StateFind.FIND_GROUPHEADER -> {

                    when (tag) {
                        "0_GROUPHEADER" -> {
                            ev = flushSection(ev, idFile, line)
                        }

                        "HEADER" -> {}

                        "TRAILER" -> {
                            saveRecord(ev, idFile)
                            return
                        }

                        else -> {
                            ev.buffer += line
                        }
                    }
                }

                StateFind.FIND_UID -> {

                    when(tag) {
                        "B45_APPLICATION", "C55_APPLICATION" -> {
                            ev.uidRow = line
                            ev.state = StateFind.FIND_GROUPHEADER
                        }

                        "C4_ID" ->
                            if(ev.event == "1.7") {
                                ev.uidRow = line
                                ev.state = StateFind.FIND_GROUPHEADER
                            } else {
                                ev.buffer += line
                            }

                        "C17_UID", "B10_UID" -> {
                            ev.uidRow = line

                            ev.state = if(ev.event == "2.4") {
                                StateFind.FIND_GUARANTOR
                            } else {
                                StateFind.FIND_GROUPHEADER
                            }
                        }

                        else -> {
                            ev.buffer += line
                        }
                    }
                }

                StateFind.FIND_GUARANTOR -> {
                    when(tag) {
                        "B24_GUARANTOR" -> {
                            ev.guarantUid = guarantorUid(line)
                            ev.buffer += line
                            ev.state = StateFind.FIND_GROUPHEADER
                        }

                        "0_GROUPHEADER" -> {
                            ev = flushSection(ev, idFile, line)
                        }

                        "TRAILER" -> {
                            saveRecord(ev, idFile)
                            return
                        }

                        else -> {
                            ev.buffer += line
                        }

                    }
                }
            }
        }
    }

    private fun flushSection(ev: Event, idFile: Long, line: String): Event {

        saveRecord(ev, idFile)
        ev.buffer.clear()

        ev.buffer += line

        val (event, date) = findEvent(line)

        ev.event = event
        ev.dateEvent = date

        ev.uidRow = null
        ev.guarantUid = ""
        ev.state = StateFind.FIND_UID

        return ev
    }

    private fun findEvent(line: String): Pair<String, LocalDate> {
        val params = line.split('\t')

        val date = params[5].toDateByPoint()

        return Pair(params[2], date)
    }

    private fun guarantorUid(line: String): String {
        val params = line.split('\t')

        return params[2]
    }

    private fun saveRecord(ev: Event, idFile: Long) {

        if(ev.uidRow == null) return

        val idMain = getMainId(ev, idFile)

        if(idMain == null) {
            if(isNotExistsRejectFile(idFile) ) throw Exception("Не найден Main.id для ROW=$this")

            return
        }

        val session = AfinaQuery.uniqueSession()

        try {

            for(line in ev.buffer) {

                val tag = line.substringBefore('\t')
                val info = line.substringAfter('\t')

                AfinaQuery.execute(EXEC_SAVE_LINE, arrayOf(idMain, tag, info))
            }

            AfinaQuery.commitFree(session)

        } catch (e: Exception) {
            logger.error("fail saveRecord", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }
    }

    private fun getMainId(ev: Event, idFile: Long): Long? {

        val words = ev.uidRow!!.split('\t')

        return when(words[0]) {

            "C4_ID" -> {

                (AfinaQuery.selectValue(SELECT_MAIN_BY_PASSPORT,
                    arrayOf(idFile, words[5], words[6], ev.event, ev.dateEvent.toTimestamp())) as Number).toLong()
            }

            "C17_UID", "B10_UID" -> {
                val guid = words[1]

                logger.error("guid=$guid")
                logger.error("event=${ev.event}")
                logger.error("dateEvent=${ev.dateEvent}")

                (AfinaQuery.selectValue(SELECT_MAIN_BY_GUID,
                    arrayOf(idFile, guid, ev.event, ev.dateEvent.toTimestamp(), ev.guarantUid)) as? Number)?.toLong()
            }

            "B45_APPLICATION", "C55_APPLICATION" -> {
                val guid = words[4]

                logger.error("guid=$guid")
                logger.error("event=${ev.event}")
                logger.error("dateEvent=${ev.dateEvent}")

                (AfinaQuery.selectValue(SELECT_MAIN_BY_GUID,
                    arrayOf(idFile, guid, ev.event, ev.dateEvent.toTimestamp(), ev.guarantUid)) as? Number)?.toLong()
            }

            else -> throw Exception("Tag Not Found ${words[0]}")
        }
    }

    private fun isNotExistsRejectFile(idFile: Long): Boolean {
        val isExistsRejectFile = (AfinaQuery.selectValue(SELECT_EXISTS_REJECT, arrayOf(idFile) ) as Number).toInt()

        return isExistsRejectFile == 0
    }
}

fun String.indexOfRepeat(char: Char, countFind: Int = 1): Int {

    var index: Int

    var startIndex = 0

    var count = 0
    do {
        index = indexOf(char, startIndex)

        if(index < 0) return index

        count++
        startIndex = index + 1
   } while (count < countFind)

   return index
}

private enum class StateFind {
    FIND_GROUPHEADER,
    FIND_UID,
    FIND_GUARANTOR
}

private data class Event(
    var event: String = "",
    var dateEvent: LocalDate = LocalDate.MIN,
    var uidRow: String? = null,
    var state: StateFind = StateFind.FIND_GROUPHEADER,
    val buffer: MutableList<String> = ArrayList(),
    var guarantUid: String = ""
)

private fun nbkiFullFileName(name: String): String = "X:/НБКИ/${name.year()}/${name.month()}/${name.day()}/CRYPTO/$name"

private fun String.year() = this.substringAfter('_').substring(0..3)

private fun String.month() = this.substringAfter('_').substring(4..5)

private fun String.day() = this.substringAfter('_').substring(6..7)


private val logger = LoggerFactory.getLogger(LoaderNbkiFileSent::class.java)

private const val SELECT_MAIN_BY_PASSPORT = "select od.PTKB_RUTDF.getMainByPassport(?, ?, ?, ?, ?) from dual"

private const val SELECT_MAIN_BY_GUID = "select od.PTKB_RUTDF.getMainByGuid(?, ?, ?, ?, ?) from dual"

private const val EXEC_SAVE_LINE = "insert into od.PTKB_RUTDF_TEMP_FILE (ID_MAIN, TAG, INFO) values (?, ?, ?)"

private const val SELECT_EXISTS_REJECT = """
    select count(*) 
    from dual 
    where exists (select 1 
       from od.ptkb_rutdf_file f
       where f.id = ? 
         and f.REJECT_FILE is not null)
"""

private const val SELECT_FILES = """
select f.id, f.file_name
from od.ptkb_rutdf_file f
where f.state = 1
  and f.date_file > ?
order by f.date_file
"""