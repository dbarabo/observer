package ru.barabo.observer.config.cbr.ptkpsd.task.f101

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.f101.F101Xml
import ru.barabo.observer.config.cbr.f101.XmlLoaderForm101
import java.io.File
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DbSaver101Form {

    private val logger = LoggerFactory.getLogger(DbSaver101Form::class.java)!!

    fun loadSave(file101: File) {

        val f101Xml = XmlLoaderForm101<F101Xml>().load(file101)

        if(f101Xml.isUniqueDb()) {
            f101Xml.saveDbData()
        }
    }

    private fun F101Xml.isUniqueDb() = AfinaQuery.selectValue(SELECT_101F_BY_UID, arrayOf(uid) ) == null

    private const val SELECT_101F_BY_UID = "select id from od.PTKB_PTKPSD_101FORM where UID_ID = ?"

    private fun F101Xml.saveDbData() {
        val uniSession = AfinaQuery.uniqueSession()

        try {

            val idMain = saveMainTable(uniSession)

            saveTableDetails(idMain, uniSession)

            AfinaQuery.commitFree(uniSession)
        } catch (e: Exception) {
            logger.error("F101Xml.saveDbData", e)

            AfinaQuery.rollbackFree(uniSession)

            throw Exception(e.message)
        }
    }

    private fun F101Xml.saveMainTable(uniSession: SessionSetting): Number {

        logger.error("reportDate=$reportDate")
        logger.error("typeReport=$typeReport")
        logger.error("createDateTime=$createDateTime")

        val mainId = AfinaQuery.nextSequence(uniSession)

        val params :Array<Any?> =
                arrayOf(mainId, reportDate.toDateByFormatter(), typeReport, createDateTime.toDateTimeByXml(), uid)

        AfinaQuery.execute(INSERT_MAIN_TABLE, params, uniSession)

        return mainId
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    private fun String.toDateTimeByXml(): Timestamp =  Timestamp.from(LocalDateTime.parse(this.trim(), dateTimeFormatter)
            .atZone(ZoneId.systemDefault()).toInstant())


    private fun String.toDateByFormatter(): Date = Date(Date.from(LocalDate.parse(this.trim(), dateFormatter)
            .atStartOfDay(ZoneId.systemDefault()).toInstant()).time)


    private const val INSERT_MAIN_TABLE =
            "insert into od.PTKB_PTKPSD_101FORM (id, DATE_REPORT, TYPE_REPORT, CREATED_FILE_REPORT, UID_ID) values " +
                    "(?, ?, ?, ?, ?)"

    private fun F101Xml.saveTableDetails(idMain: Number, uniSession: SessionSetting) {

        saveBalance(idMain, uniSession)

        saveOffBalance(idMain, uniSession)
    }

    private fun F101Xml.saveBalance(idMain: Number, uniSession: SessionSetting) {
        data101?.balance?.balanceList?.forEach {

            val params: Array<Any?> = arrayOf(idMain, "Баланс", it.account5, it.currency, it.activePassive, it.restIn,
                    it.restOut, it.turnDebet, it.turnCredit)

            AfinaQuery.execute(INSERT_DETAIL_TABLE, params, uniSession)
        }
    }

    private fun F101Xml.saveOffBalance(idMain: Number, uniSession: SessionSetting) {
        data101?.offBalanceGroup?.offBalanceList?.forEach {

            val params: Array<Any?> = arrayOf(idMain, "Внебал", it.account5, it.currency, it.activePassive, it.restIn,
                    it.restOut, it.turnDebet, it.turnCredit)

            AfinaQuery.execute(INSERT_DETAIL_TABLE, params, uniSession)
        }
    }

    private const val INSERT_DETAIL_TABLE =
            "insert into od.PTKB_PTKPSD_101FORM_DETAIL (id, ptkpsd_101form, type_balance, account, currency, " +
                    "active_passive, rest_in, rest_out, turn_debet, turn_credit) values " +
                    "(classified.nextval, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?)"
}