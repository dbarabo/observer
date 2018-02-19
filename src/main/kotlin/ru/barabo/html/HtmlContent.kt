package ru.barabo.html

data class HtmlContent(private val title: String,
                       private val body: String,
                       private val headerTable: Map<String, String>,
                       private val data: List<Array<Any?>>) {

    private fun htmlHeader() :String = headerTable.keys.joinToString("\n</th><th>", "\n<tr><th>", "\n</th></tr>")

    private fun tableRow(row: Array<Any?>) :String {

        var index = -1

        return headerTable.values.joinToString("") {
            index++

            "<td align=\"$it\">${toStringNull(row[index])}</td>\n"
        }
    }

    private fun toStringNull(value: Any?) = value?.toString() ?:""

    private fun tableData(): String =
        data.joinToString("\t</tr>\n\t<tr>", "\n\t<tr>", "\n\t</tr>") { tableRow(it) }

    fun html() :String {
        val headerBody = htmlHeader() + tableData()

        return "<html> <head>  <title> $title </title> <body> $body <table border=\"1\"> $headerBody </table>  </body> </html>"
    }
}