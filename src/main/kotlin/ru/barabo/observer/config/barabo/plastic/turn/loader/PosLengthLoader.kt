package ru.barabo.observer.config.barabo.plastic.turn.loader

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.nio.charset.Charset

interface PosLengthLoader {

    val headerColumns :Array<Column>

    val bodyColumns :Array<Column>

    val tailColumns :Array<Column>

    val headerQuery :String?

    val bodyQuery :String?

    val tailQuery :String?

    fun load(file: File, charset: Charset) {

        val sessionSetting = AfinaQuery.uniqueSession()

        //LoggerFactory.getLogger(PosLengthLoader::class.java).error("sessionSetting.id=${sessionSetting.idSession}")

        var order :Int = 0

        var headerId :Any? = null

        try {
            file.forEachLine(charset) {

                when (getTypeLine(it, order)) {
                    TypeLine.HEADER-> {
                        headerId = processHeader(it, sessionSetting)
                    }
                    TypeLine.BODY-> {
                        processBody(it, headerId, sessionSetting)
                    }
                    TypeLine.TAIL-> {
                        processTail(it, headerId, sessionSetting)
                    }
                    TypeLine.NOTHING->{}
                }
                order++
            }

        } catch (e :Exception) {

            LoggerFactory.getLogger(PosLengthLoader::class.java).error("load", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw Exception(e.message)
        }

        AfinaQuery.commitFree(sessionSetting)
    }

    private fun processTail(line :String, headerId :Any?, sessionSetting :SessionSetting) {

        processLine(tailQuery, line, headerId, tailColumns, sessionSetting)
    }

    private fun processBody(line :String, headerId :Any?, sessionSetting :SessionSetting) {

        processLine(bodyQuery, line, headerId, bodyColumns, sessionSetting)
    }

    fun generateHeaderSequense(line :String, sessionSetting :SessionSetting) :Any? =
            AfinaQuery.nextSequence(sessionSetting)

    private fun processHeader(line :String, sessionSetting :SessionSetting) :Any? {

        val id = generateHeaderSequense(line, sessionSetting)

        processLine(headerQuery, line, id, headerColumns, sessionSetting, false)

        return id
    }

    private fun processLine(query :String?, line :String, id :Any?, columns :Array<Column>,
                            sessionSetting :SessionSetting, isExecOnlyExistsValues :Boolean = true) {

        if(query == null) return

        val values = parseLine(line, columns)

        if(isExecOnlyExistsValues && values.isEmpty()) return

        val params :MutableList<Any> = ArrayList<Any>()

        id?.let { params.add(it) }

        params.addAll(values)

        AfinaQuery.execute(query, params.toTypedArray(), sessionSetting)
    }

    private fun parseLine(line :String,  columns :Array<Column>) :List<Any> {

//        columns.forEach {
//            val value = it.convertToDb(
//                    if(line.length < it.position + it.length) null
//                    else line.substring(it.position, it.position + it.length)
//            )
//
//            LoggerFactory.getLogger(PosLengthLoader::class.java).error(line.substring(it.position, it.position + it.length))
//            LoggerFactory.getLogger(PosLengthLoader::class.java).error(value.toString())
//        }

        return columns.map { it.calculate(line) }
    }

    fun getTypeLine(line :String, order :Int) :TypeLine

}

enum class TypeLine {
    HEADER,
    BODY,
    TAIL,
    NOTHING
}

data class Column(private val position: Int,
                  private val length: Int,
                  private val convertToDb: (String?)->Any) {

    fun calculate(line :String) :Any {

        LoggerFactory.getLogger(Column::class.java).error("position=$position length=$length " +
                "convert= ${if(line.length < position + length) null else line.substring(position, position + length)}"
        )

        return convertToDb(if(line.length < position + length) null else line.substring(position, position + length) )
    }
}