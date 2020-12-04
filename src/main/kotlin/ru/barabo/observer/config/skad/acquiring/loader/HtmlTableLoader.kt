package ru.barabo.observer.config.skad.acquiring.loader

import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

private val logger = LoggerFactory.getLogger(ClearIntLoaderImpl::class.java)

class ClearIntLoaderImpl : ClearIntLoader {

    override fun load(file: File, charset: Charset): ClearIntInfo {
        val allLines = file.readLines(charset)

        val (date, startIndex) = findMainDate(allLines) ?: return ClearIntInfo(null, emptyList())

        var index = startIndex

        var paySystem = ""

        val headerInfoList = ArrayList<HeaderInfo>()

        while (index < allLines.size) {

            val (headerInfo, newIndex) = findHeaderInfo(allLines, index, paySystem)

            index = newIndex

            if(headerInfo == emptyHeaderInfo) continue

            if(headerInfo.paySystem.isNotEmpty()) {
                paySystem = headerInfo.paySystem
            }

            headerInfoList += headerInfo
        }

        return ClearIntInfo(date, headerInfoList)
    }

    private fun findMainDate(allLines: List<String>): Pair<LocalDate, Int>? {

        var positionLoop = 0

        while (positionLoop < allLines.size) {

            val (text, position) = findH1(allLines, positionLoop) ?: return null

            positionLoop = position

            val regexMatcher = regexDateSlash.matcher(text)
            if(regexMatcher.find()) {
                val date = LocalDate.parse(regexMatcher.group(1), dateSlashFormatter)

                return Pair(date, positionLoop)
            }
        }
        return null
    }

    private fun findHeaderInfo(allLines: List<String>, startIndex: Int, priorPaySystem: String): Pair<HeaderInfo, Int> {

        var index = startIndex

        val (header1, newPaySystem) = findH1AndPaySystem(allLines[index], priorPaySystem)

        var h1 = header1

        if(h1.isNotEmpty()) index++

        var h2: String = findH2(allLines[index])

        if(h1.isEmpty() && h2.isEmpty()) return Pair(emptyHeaderInfo, index+1)

        if(newPaySystem != priorPaySystem) {
            h1 = h2
            h2 = ""
        }

        val(table, newIndex, newH2) = findTable(allLines, index + 1)

        if(newH2.isNotEmpty() && table == emptyTable) {
            if(h2.isEmpty()) {
                h2 = newH2
            } else if(h1.isEmpty()) {
                h1 = h2
                h2 = newH2
            }
        }

        return Pair(HeaderInfo(newPaySystem, h1, h2, table), newIndex)
    }

    private fun findH1AndPaySystem(line: String, oldPaySystem: String): Pair<String, String> {
        val regexMatcherH1 = regexH1Extract.matcher(line)

        if(!regexMatcherH1.find()) return Pair("", oldPaySystem)

        val h1 = regexMatcherH1.group(1)

        var newPaySystem = oldPaySystem

        if(h1.indexOf(startPaySystem) == 0) {
            newPaySystem = h1.substring(startPaySystem.length).trim()
        }

        return Pair(h1, newPaySystem)
    }

    private fun findH1(allLines: List<String>, startIndex: Int): Pair<String, Int>? {

        var index = startIndex

        while (index < allLines.size) {
            val regexMatcher = regexH1Extract.matcher(allLines[index])

            if(regexMatcher.find()) {
                val header = regexMatcher.group(1)

                return Pair(header, index + 1)
            }
            index++
        }
        return null
    }

    private fun findH2(line: String): String = regexH2Extract.matcher(line).takeIf { it.find() }?.group(1) ?: ""

    private fun findTable(allLines: List<String>, startIndex: Int): Triple<HtmlTable, Int, String> {

        val findTableEnd = findTableEndFirst(allLines, startIndex)
        if(findTableEnd != null) return findTableEnd

        var index = startIndex + 1

        val headerTable: String = regexTableHeader.matcher( allLines[index] ).takeIf { it.find() }?.group(1)
            ?: return Triple(emptyTable, index+1, "")

        val headers = headerTable.split("</td><td>").filter { it.isNotEmpty() }

        val rows = ArrayList<List<String>>()

        do {
            index++

            val dataRow = regexTableRow.matcher( allLines[index] ).takeIf { it.find() }?.group(1)
                ?: break

            val matcher = regexTableCell.matcher(dataRow)

            val rowData = ArrayList<String>()

            while(matcher.find()) {
                rowData += matcher.group(matcher.groupCount())
            }
            rows += rowData

        } while (index < allLines.size)

        while(index < allLines.size && allLines[index] != tableEnd) {
            index++
        }

        if(rows.isEmpty() ) return Triple(emptyTable, index+1, "")

        val types = createTypesByData(rows[0])

        return Triple(HtmlTable(headers, types, rows), index+1, "")
    }

    private fun createTypesByData(row: List<String>): List<HTypes> {

        val types = ArrayList<HTypes>()

        for(cell in row) {

            types += when {
                cell.toIntOrNull() != null -> HTypes.INT
                cell.toDoubleOrNull() != null -> HTypes.MONEY
                cell.length == 10 && regexDateDot.matcher(cell).find() -> HTypes.DATE
                else -> HTypes.STRING
            }
        }

        return types
    }

    private fun findTableEndFirst(allLines: List<String>, startIndex: Int): Triple<HtmlTable, Int, String>? {

        if(allLines[startIndex].trim() == tableStart) return null

        var index = startIndex

        if(allLines[index].trim() != tableEnd){
            val header = allLines[index]
            index++

            return Triple(emptyTable, index + 1, if(allLines[index].trim()  == tableEnd) header else "")
        }

        index++

        return if(allLines[index].indexOf("<h") == 0) Triple(emptyTable, index, "")
            else Triple(emptyTable, index+1, allLines[index])
    }
}


private val regexDateDot = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})")

private val regexTableHeader = Pattern.compile("<tr class=\"th\"><td>(.*?)</td></tr>")

private val regexTableCell =  Pattern.compile("(<td[^>]*>(.*?)</td>)")

private val regexTableRow = Pattern.compile("<tr class=\"tr\">(.*?)</tr>")

private const val tableStart: String = "<table class=\"tbl\">"

private const val tableEnd: String = "</table>"

private const val startPaySystem = "Платежная система"

private val regexH2Extract = Pattern.compile("<h2 class=\"h2\">(.*?)</h2>")

private val dateSlashFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val regexH1Extract = Pattern.compile("<h1 class=\"h1\">(.*?)</h1>")

private val regexDateSlash = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})")

interface ClearIntLoader {
    fun load(file: File, charset: Charset): ClearIntInfo
}

data class ClearIntInfo(val date: LocalDate?, val headers: List<HeaderInfo>)

data class HeaderInfo(var paySystem: String, var h1: String, var h2: String, var table: HtmlTable)

data class HtmlTable(val headers: List<String>, val types: List<HTypes>, val rows: List<List<String>>)

val emptyTable: HtmlTable = HtmlTable(emptyList(), emptyList(), emptyList())

private val emptyHeaderInfo = HeaderInfo("", "", "", emptyTable)

enum class HTypes {
    STRING,
    DATE,
    INT,
    MONEY
}
