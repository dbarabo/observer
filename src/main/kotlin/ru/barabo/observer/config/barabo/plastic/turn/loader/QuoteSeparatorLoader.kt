package ru.barabo.observer.config.barabo.plastic.turn.loader

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionException
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*

interface QuoteSeparatorLoader {

    val headerColumns: Map<Int, (String?)->Any>

    val bodyColumns: Map<Int, (String?)->Any>

    val tailColumns: Map<Int, (String?)->Any>

    val headerQuery: String?

    val bodyQuery: String?

    val tailQuery: String?

    fun separator(): String = ";"

    private fun csvQuote() = "${separator()}(?=([^\"]*\"[^\"]*\")*[^\"]*$)"

    private fun parseCsvQuote(lineIn: String): List<String> = lineIn.split(csvQuote().toRegex())

    fun parseInt(value :String?): Any {
        val length = value?.trim()?.length?:0

        if(length == 0) return Double::class.javaObjectType

        return if(length < 18) value?.trim()?.toLong()?:0 else BigInteger(value?.trim())
    }

    fun parseToString(value :String?): Any = value?.trim()?.let{ it } ?: String::class.javaObjectType

    fun load(file: File, charset: Charset) {

        val sessionSetting = AfinaQuery.uniqueSession()

        var order = 0

        var headerId :Any? = null

        try {
            file.forEachLine(charset) {

                val fields = parseCsvQuote(it)

                when (getTypeLine(fields, order)) {
                    TypeLine.HEADER-> {
                        headerId = processHeader(fields, sessionSetting)
                    }
                    TypeLine.BODY-> {
                        processBody(fields, headerId, sessionSetting)
                    }
                    TypeLine.TAIL-> {
                        processTail(fields, headerId, sessionSetting)
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

    fun generateHeaderSequence(fields :List<String>, sessionSetting :SessionSetting) :Any?
            = AfinaQuery.nextSequence(sessionSetting)

    private fun processHeader(fields :List<String>, sessionSetting :SessionSetting) :Any? {

        val id = generateHeaderSequence(fields, sessionSetting)

        processLine(fields, headerQuery, id, headerColumns, sessionSetting, false)

        return id
    }

    private fun processBody(fields :List<String>, headerId :Any?, sessionSetting :SessionSetting) {

        processLine(fields, bodyQuery, headerId, bodyColumns, sessionSetting)
    }

    private fun processTail(fields :List<String>, headerId :Any?, sessionSetting :SessionSetting) {

        processLine(fields, tailQuery, headerId, tailColumns, sessionSetting)
    }

    fun getTypeLine(fields :List<String>, order :Int) :TypeLine

    private fun processLine(fields: List<String>, query: String?, id: Any?, columns: Map<Int, (String?)->Any>,
                            sessionSetting: SessionSetting, isExecOnlyExistsValues: Boolean = true) {

        if(query == null) return

        val values = valuesByColumns(fields, columns)

        if(isExecOnlyExistsValues && values.isEmpty()) return

        val params: MutableList<Any> = ArrayList()

        id?.let { params.add(it) }

        params.addAll(values)

        try {
            AfinaQuery.execute(query, params.toTypedArray(), sessionSetting)
        } catch (e: Exception) {
            LoggerFactory.getLogger(QuoteSeparatorLoader::class.java).error("processLine", e)

            LoggerFactory.getLogger(QuoteSeparatorLoader::class.java).error("query=$query")

            LoggerFactory.getLogger(QuoteSeparatorLoader::class.java).error("params=$params")

            throw Exception(e)
        }

    }

    private fun valuesByColumns(fields :List<String>, columns :Map<Int, (String?)->Any>) :List<Any> {

      return columns.entries.map { it.value.invoke( if(it.key >= 0 && it.key < fields.size) fields[it.key] else "") }
    }


}
