package ru.barabo.observer.config.task.info

import ru.barabo.cmd.Cmd
import ru.barabo.html.HtmlContent
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.TreeElem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object InfoHtmlData {

    fun sendInfo(date: LocalDate, root: TreeElem) {

        if(root.isEmptyElemTask()) return

        val title = "Отчет по сборке ${TaskMapper.build()} за ${date.formatDay()}"

        val body = "Ежедневный Итоговый отчет по сборке ${TaskMapper.build()} за ${date.formatDay()}"

        val data = createHtmlData(title, body, root)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.OPER_YA, subject = title, body = data, subtypeBody = "html")
    }

    private fun TreeElem.isEmptyElemTask(): Boolean = group?.childs?.firstOrNull() === null

    private fun LocalDate.fileNameHtml() = "${TaskMapper.build().toLowerCase()}-${formatFileName()}.html"

    private fun LocalDate.formatFileName() = DateTimeFormatter.ofPattern("yyyyMMdd").format(this)

    private fun LocalDate.formatDay() = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(this)

    private fun folderPath() = Cmd.LOG_FOLDER

    private fun createHtmlData(title: String, body: String, root: TreeElem): String {
        val data = processData(root)

        val content = HtmlContent(title, body, headerTable, data)

        return content.html()
    }

    private fun processData(root: TreeElem): List<Array<Any?>> {

        val result = ArrayList<Array<Any?>>()

        root.group?.childs?.forEach {
            result += addConfig(it.group?.config)

            it.group?.childs?.forEach { result += it.addRowInfo() }
        }

        return result
    }

    private fun TreeElem.addRowInfo(): Array<Any?> = arrayOf("........$task", state, count, created, executed, name, id)

    private fun addConfig(config: ConfigTask?): Array<Any?> =
            arrayOf("<b>${config?.name()?.toUpperCase()}</b>", null, null, null, null, null, null)

    private val headerTable = mapOf(
            "Задача" to "left",
            "Статус" to "center",
            "Кол-во" to "right",
            "Создан" to "center",
            "Обработан" to "center",
            "Имя" to "left",
            "id" to "right")

}