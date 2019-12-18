package ru.barabo.observer.config.barabo.plastic.release.task

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object OutSalaryResponseFile : SingleSelector {
    override val select: String = "{ ? = call od.ptkb_plastic_load.getSalaryResponseOpenCard }"

    override fun isCursorSelect(): Boolean = true

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, workTimeFrom = LocalTime.of(7, 0))

    override fun name(): String = "Отправить ответ по зарплат. файлу"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun execute(elem: Elem): State {
        val companyData = AfinaQuery.execute(EXEC_READ_COMPANY, arrayOf(elem.idElem), SessionSetting(false),
                intArrayOf(OracleTypes.VARCHAR, OracleTypes.VARCHAR)) ?: throw Exception("request is null $EXEC_READ_COMPANY")

        val data = AfinaQuery.selectValue(SELECT_RESPONSE_XML, arrayOf(elem.idElem) ) as? Clob
                ?: throw Exception("response is null $SELECT_RESPONSE_XML id=${elem.idElem}")

        val company = companyData[0] as? String

        val inn = companyData[1] as? String

        val fileXml = File("${IbiSendToJzdo.hCardOutSentTodayByFolder()}/resp${nowDayHour()}.xml")

        fileXml.writeText(data.clobToString(), Charset.forName("cp1251"))

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.IBANK_RECEIPTOR, cc = BaraboSmtp.IBANK_DELB, bcc = BaraboSmtp.OPER_YA,
                subject = subjectFile(company, inn), body = bodyFile(company, inn, fileXml.name), attachments = arrayOf(fileXml))

        AfinaQuery.execute(EXEC_UPDATE_STATE, arrayOf(elem.idElem))

        return State.OK
    }

    private fun subjectFile(company: String?, inn: String?): String = "Ответ по групп. открытию карт $company ИНН:$inn"

    private fun bodyFile(company: String?, inn: String?, fileName: String): String =
            """Это письмо содержит вложение с ответным файлом $fileName на групповое открытие карт для компании $company ИНН:$inn
Данное вложение нужно переслать компании по интернет-банку"""

    private fun nowDayHour(): String = DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now())

    private const val EXEC_READ_COMPANY = "{ call od.ptkb_plastic_load.readCompanyByResponseDoc(?, ?, ?) }"

    private const val EXEC_UPDATE_STATE = "update od.PTKB_PLASTIC_OUTCARD_RESPONSE set state = 1 where response_doc = ?"

    private const val SELECT_RESPONSE_XML = "select od.ptkb_plastic_load.response1CCard( ? ) from dual"
}