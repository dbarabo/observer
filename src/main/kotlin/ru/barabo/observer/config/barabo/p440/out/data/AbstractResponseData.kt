package ru.barabo.observer.config.barabo.p440.out.data

import org.slf4j.LoggerFactory
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class AbstractResponseData : ResponseData {

    private val logger = LoggerFactory.getLogger(AbstractResponseData::class.java)

    private lateinit var fromFns: Number

    private lateinit var fileNameResponse: String

    private lateinit var fileNameResponseTemplate: String

    private lateinit var fileNameFromFns: String

    private lateinit var isSmevSource: Number

    private lateinit var versionRequestInfo: String

    override fun idFromFns(): Number = fromFns

    override fun fileNameResponse(): String = fileNameResponse

    override fun fileNameResponseTemplate(): String = fileNameResponseTemplate

    override fun fileNameFromFns(): String = fileNameFromFns

    override fun isSourceSmev(): Boolean = isSmevSource.toInt() != 0

    override fun versionRequest(): String = versionRequestInfo

    fun isPbFile(): Boolean = fileNameResponse.indexOf("PB") == 0

    private fun selectResponse(addSeparFields :String, addSeparTables :String, addWhere :String) :String =
            "select r.FNS_FROM, f.VERSION, r.FILE_NAME, f.FILE_NAME, f.type_assoc $addSeparFields " +
            "from od.PTKB_440P_RESPONSE r, od.PTKB_440P_FNS_FROM f $addSeparTables " +
            "where r.id = ? and r.fns_from = f.id $addWhere"

    //select r.FNS_FROM, r.IS_PB,

    protected open fun addSeparFields(): String = ""

    protected open fun addSeparTables(): String = ""

    protected open fun addWhere(): String = ""

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {

        val selectQuery = selectResponse(addSeparFields(), addSeparTables(), addWhere() )

        logger.error("selectQuery=$selectQuery")
        logger.error("idResponse=$idResponse")

        val rowData = AfinaQuery.select(selectQuery, arrayOf(idResponse) )[0]

        fillDataFields(idResponse, rowData, sessionSetting)

        return this
    }

    /**
     * default first select // r.FNS_FROM, r.IS_PB, r.FILE_NAME, f.FILE_NAME, f.type_assoc
     */
    protected open fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        fromFns = rowData[0] as Number

        versionRequestInfo =  rowData[1]?.let { it as String } ?: "3.01"

        fileNameResponseTemplate = (rowData[2] as String).substringBeforeLast(".")

        fileNameResponse = String.format("$fileNameResponseTemplate.xml", dateFormatInFile())

        fileNameFromFns = (rowData[3] as String).substringBeforeLast(".")

        isSmevSource = (rowData[4] as? Number) ?: 0
    }
}

fun dateFormatInFile(): String = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())
