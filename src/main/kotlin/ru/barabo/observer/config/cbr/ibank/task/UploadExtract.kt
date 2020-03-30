package ru.barabo.observer.config.cbr.ibank.task

import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.cbr.correspondent.task.DownLoadToCorrespond.dTaBack
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.sql.Clob
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object UploadExtract : SinglePerpetual {
    private val logger = LoggerFactory.getLogger(UploadExtract::class.java)

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 1

    override fun config(): ConfigTask = IBank

    override fun name(): String = "Выгрузка выписок"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(0, 30), workTimeTo = LocalTime.of(23, 30) )

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(CREATE_OPER_EXTRACT)

        val accounts = AfinaQuery.selectCursor(SELECT_EXTRACT_ACCOUNTS)

        if(accounts.isEmpty() ) return super.execute(elem)

        val copyFolder = dTaBack().byFolderExists()

        for (account in accounts) {

            if(account.isEmpty()) continue

            account[0]?.let {
                extractAccount(it as Number, copyFolder.absolutePath)
            }
        }
        return super.execute(elem)
    }

    private fun extractAccount(accountId: Number, tempFolderPath: String) {
        val session = AfinaQuery.uniqueSession()

        try {
            val data = AfinaQuery.execute(EXEC_EXTRACT_ACCOUNT, arrayOf(accountId), session, intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

            val fileName = data!![1] as? String

            createFile(fileName, (data[0] as? Clob)?.clob2string(), tempFolderPath)

            AfinaQuery.commitFree(session)
        } catch (e: Exception) {
            logger.error("extractAccount", e)

            AfinaQuery.rollbackFree(session)

            throw Exception(e)
        }
    }

    private fun createFile(fileName: String?, data: String?, tempFolderPath: String) {
        if(fileName == null || data == null) return

        val file = File("$tempFolderPath/$fileName")

        if(file.exists()) throw IOException("file already exists $file")

        val dataFile = HEADER_XML + data.replace("([\r\n])".toRegex(), "")

        file.writeText(dataFile, Charset.forName("CP1251"))

        val fakturaPathFile = File("${LoanInfoSaver.PATH_FAKTURA_OUTBOX}${file.name}")

        file.copyTo(fakturaPathFile, true)
    }

    private const val HEADER_XML = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>"

    private const val CREATE_OPER_EXTRACT = "{ call od.PTKB_IBANK.createOperAll }"

    private const val SELECT_EXTRACT_ACCOUNTS = "{ ? = call od.PTKB_IBANK.getAccountsForExtract }"

    private const val EXEC_EXTRACT_ACCOUNT = "{ call od.PTKB_IBANK.getOperExtractByAccount(?, ?, ?) }"
}