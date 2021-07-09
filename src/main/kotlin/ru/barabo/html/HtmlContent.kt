package ru.barabo.html

data class HtmlContent(private val title: String,
                       private val body: String,
                       private val headerTable: Map<String, String>,
                       private val data: List<Array<Any?>>) {

    fun htmlBodyOnly(): String {
        val headerBody = htmlHeader() + tableData()

        return "<body> $body <table border=\"1\"> $headerBody </table>  </body> "
    }

    fun html(): String {
        val headerBody = htmlHeader() + tableData()

        return "<html> <head>  <title> $title </title> ${tableStyle()} </head> <body> $body <table border=\"1\"> $headerBody </table>  </body> </html>"
    }

    private fun htmlHeader(): String = headerTable.keys.joinToString("\n</th><th>", "\n<tr><th>", "\n</th></tr>")

    private fun tableData(): String =
        data.joinToString("\t</tr>\n\t<tr>", "\n\t<tr>", "\n\t</tr>") { tableRow(it) }

    private fun tableRow(row: Array<Any?>): String {

        var index = -1

        return headerTable.values.joinToString("") {
            index++

            "<td align=\"$it\">${toStringNull(row[index])}</td>\n"
        }
    }

    private fun toStringNull(value: Any?) :String {
        val text = value?.toString() ?:" "

        return if(text.isEmpty()) " " else text
    }

}

fun htmlHeaderOnly(): String = "<html> <head>  <title> </title> ${tableStyle()} </head>"

private fun tableStyle(): String = "<style> table { width: 100%; border: 4px double black; border-collapse: collapse; } " +
        "th {border: 1px solid black; } td { border: 1px solid black; } </style>"