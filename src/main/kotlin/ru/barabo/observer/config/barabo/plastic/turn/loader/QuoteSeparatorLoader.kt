package ru.barabo.observer.config.barabo.plastic.turn.loader

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern


interface QuoteSeparatorLoader {

    val headerColumns :Map<Int, (String?)->Any>

    val bodyColumns :Map<Int, (String?)->Any>

    val tailColumns :Map<Int, (String?)->Any>

    val headerQuery :String?

    val bodyQuery :String?

    val tailQuery :String?

    var patternCsvQuote :Pattern?

    fun separator() :String = ";"

    private fun csvQuote() = "(\"([^\"]*)\"|[^${separator()}]*)(${separator()}|$)"

    private fun patternCsvQuote() = Pattern.compile(csvQuote())

    private fun parseCsvQuote(lineIn: String) :List<String> {

        val line = lineIn.replace("\"".toRegex(), "\n")

        patternCsvQuote = patternCsvQuote?.let { patternCsvQuote } ?:patternCsvQuote()

        val matcher = patternCsvQuote!!.matcher(line)

        val fields = ArrayList<String>()

        while (matcher.find()) {
            var group = matcher.group(1)?:null

            if (group?.isNotEmpty() == true && '"' == group[0]) {
                group = matcher.group(2)
            }

            group = group?.replace("\n".toRegex(), "\"")

            fields.add(group?:"")
        }

        return fields
    }

    fun parseInt(value :String?) :Any {
        val length = value?.trim()?.length?:0

        if(length == 0) return Double::class.javaObjectType

        return if(length < 18) value?.trim()?.toLong()?:0 else BigInteger(value?.trim())
    }

    fun parseToString(value :String?) :Any = value?.trim()?.let{ it } ?: String::class.javaObjectType

    fun load(file: File, charset: Charset) {

        val sessionSetting = AfinaQuery.uniqueSession()

        //LoggerFactory.getLogger(QuoteSeparatorLoader::class.java).error("sessionSetting.id=${sessionSetting.idSession}")

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

    private fun processLine(fields :List<String>, query :String?, id :Any?, columns :Map<Int, (String?)->Any>,
                            sessionSetting :SessionSetting, isExecOnlyExistsValues :Boolean = true) {

        if(query == null) return

        val values = valuesByColumns(fields, columns)

        if(isExecOnlyExistsValues && values.isEmpty()) return

        val params :MutableList<Any> = ArrayList()

        id?.let { params.add(it) }

        params.addAll(values)

        AfinaQuery.execute(query, params.toTypedArray(), sessionSetting)
    }

    private fun valuesByColumns(fields :List<String>, columns :Map<Int, (String?)->Any>) :List<Any> {

      return columns.entries.map { it.value.invoke( if(it.key >= 0 && it.key < fields.size) fields[it.key] else "") }
    }


}
