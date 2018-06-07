package ru.barabo.observer.config.cbr.other.task

import ru.barabo.html.HtmlContent
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.util.*

object CheckActializationEntryDouble: SingleSelector {

    override val select: String =
              """select dta.classified, to_char(dta.operdate, 'yy.mm.dd') || ' ' || dta.label
                   from doctree dta
                  where dta.doctype = 1007403095 /*archiveType*/
                    and dta.docstate = 1000000035
                    and dta.sysfilial = 1
                    and dta.operdate > sysdate - 20
                    and trunc(dta.userdate) = trunc(sysdate)

                   and exists(
                       select 1
                        from doctree ge
                           , doctree chi
                        where ge.parent = dta.classified
                          and ge.genintention = 1000000020
                          and chi.parent = ge.classified
                          and chi.doctype =  1000135286
                          and chi.docstate = 1000000035
                          )"""

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(11, 0),
            workTimeTo = LocalTime.of(22, 0))

    override fun name(): String = "Проверка дубля Актуализации"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem): State {

        val truncOperDate = AfinaQuery.selectValue(SELECT_OPERDATE, arrayOf(elem.idElem)) as Date?

        val data = AfinaQuery.select(SELECT_DOUBLE_ACTIALIZATION, arrayOf(truncOperDate, truncOperDate))

        return if(data.isNotEmpty()) sendReport(data, truncOperDate) else State.OK
    }

    private const val SELECT_OPERDATE = "select trunc(cho.ARCDATE) from CHANGEOPERDATE cho where cho.doc = ? and rownum = 1"

    private const val SELECT_DOUBLE_ACTIALIZATION =
             """select trunc(ge.operdate), count(*), min(ge.classified), max(ge.classified)
                  from doctree ge
                 where ge.genintention = 1000000020
                   and ge.operdate >= ? and ge.operdate < (? + 1)

                   and exists (
                      select 1
                        from doctree chi
                       where chi.parent = ge.classified
                         and chi.doctype =  1000135286
                         and chi.docstate = 1000000035)
                 group by trunc(ge.operdate)
                 having count(*) > 1"""

    private fun sendReport(data: List<Array<Any?>>, date: Date?): State {

        val title = "Дублирование Общих пап с проводками Актуализации в $date опердне"

        val report = createHtmlData(title, data)

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = title, body = report, subtypeBody = "html")

        return State.ERROR
    }

    private fun createHtmlData(title: String, data: List<Array<Any?>>): String {

        val content = HtmlContent(title, title, headerTable, data)

        return content.html()
    }

    private val headerTable = mapOf(
            "Дата проводок" to "center",
            "Кол-во Общих Пап" to "right",
            "Min id Папы" to "right",
            "Max id Папы" to "right")
}