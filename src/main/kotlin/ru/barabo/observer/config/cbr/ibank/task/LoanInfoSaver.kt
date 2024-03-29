package ru.barabo.observer.config.cbr.ibank.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.afina.selectValueType
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.cbr.ptkpsd.task.SendByPtkPsdNoXml
import ru.barabo.observer.config.cbr.ptkpsd.task.SendByPtkPsdNoXml.dArchiveOutToday
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalTime

object LoanInfoSaver : SingleSelector {

    override fun config(): ConfigTask = IBank

    override fun name(): String = "Выгрузка инфо по кредитам LoanInfo"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(9, 0), workTimeTo = LocalTime.of(19, 0) )

    override val select: String = "select id, FILENAME from od.PTKB_LOAN_INFO where STATUS = 0"

    override fun execute(elem: Elem): State {

        val clob = selectValueType<Clob>(SELECT_CLOB, arrayOf(elem.idElem)) ?: return State.ERROR

        val fileArchive = File("${dArchiveOutTodayFolder()}/${elem.name}")

        val fileSave = File("$PATH_FAKTURA_OUTBOX${elem.name}")

        fileArchive.writeText(clob.clobToString(), Charset.forName("cp1251"))

        fileArchive.copyTo(fileSave, true)

        AfinaQuery.execute(EXECUTED_STATE_CLOB, arrayOf(elem.idElem))

        return State.OK
    }

    private fun dArchiveOutTodayFolder(): String = dArchiveOutToday().byFolderExists().absolutePath

    private const val SELECT_CLOB = "select LOANINFOTEXT from od.PTKB_LOAN_INFO where ID = ?"

    val PATH_FAKTURA_OUTBOX = "\\\\192.168.0.31\\outbox\\".ifTest("C:\\temp\\")

    private const val EXECUTED_STATE_CLOB = "update od.PTKB_LOAN_INFO set STATUS = 1 where id = ?"
}