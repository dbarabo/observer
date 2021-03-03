package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class AbstractResponseData : ResponseData {

    private lateinit var fromFns: Number

    private lateinit var fileNameResponse: String

    private lateinit var fileNameResponseTemplate: String

    private lateinit var fileNameFromFns: String

    override fun idFromFns(): Number = fromFns

    override fun fileNameResponse(): String = fileNameResponse

    override fun fileNameResponseTemplate(): String = fileNameResponseTemplate

    override fun fileNameFromFns(): String = fileNameFromFns

    private fun selectResponse(addSeparFields :String, addSeparTables :String, addWhere :String) :String =
            "select r.FNS_FROM, r.IS_PB, r.FILE_NAME, f.FILE_NAME $addSeparFields " +
            "from od.PTKB_440P_RESPONSE r, od.PTKB_440P_FNS_FROM f $addSeparTables " +
            "where r.id = ? and r.fns_from = f.id $addWhere"


    protected open fun addSeparFields(): String = ""

    protected open fun addSeparTables(): String = ""

    protected open fun addWhere(): String = ""

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {
        val rowData = AfinaQuery.select(
                selectResponse(addSeparFields(), addSeparTables(), addWhere() ),
                arrayOf(idResponse) )[0]

        fillDataFields(idResponse, rowData, sessionSetting)

        return this
    }

    companion object {

        fun dateFormatInFile() = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now())!!
    }

    /**
     * default first select // r.FNS_FROM, r.IS_PB, r.FILE_NAME, f.FILE_NAME,
     */
    protected open fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        fromFns = rowData[0] as Number

        fileNameResponseTemplate = (rowData[2] as String).substringBeforeLast(".")

        fileNameResponse = String.format("$fileNameResponseTemplate.xml", dateFormatInFile())

        fileNameFromFns = (rowData[3] as String).substringBeforeLast(".")
    }
}