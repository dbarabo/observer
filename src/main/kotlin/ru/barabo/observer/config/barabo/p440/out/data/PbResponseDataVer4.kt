package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.task.p440.out.xml.pb.PbResult
import java.util.*

class PbResponseDataVer4 : AbstractResponseData() {

    override fun xsdSchema(): String = "/xsd/440-П_PB.xsd"

    override fun typeInfo(): String = "ПОДБНПРИНТ"

    override fun addSeparFields(): String =
        ", f.CHECK_CODES, f.CHECK_TEXT_ERRORS, f.CHECK_ATTR_ERROR, f.CHECK_ATTR_RES_ERROR"


    private lateinit var pbResult : List<PbResult>

    fun getPbResultList() : List<PbResult> = pbResult

    override fun fillDataFields(idResponse: Number, rowData :Array<Any?>, sessionSetting: SessionSetting) {

        super.fillDataFields(idResponse, rowData, sessionSetting)

        val checkCodes = rowData[4] as String

        val checkTextErrors = rowData[5] as? String

        val checkAttributeCodes = rowData[6] as? String

        val checkAttributeValues = rowData[7] as? String

        pbResult = generatePbResult(checkCodes, checkTextErrors, checkAttributeCodes, checkAttributeValues)
    }

    private fun generatePbResult(checkCodes: String, checkTextErrors: String?,
                                 checkAttributeCodes: String?, checkAttributeValues: String?) : List<PbResult> {

        val pbResultList : MutableList<PbResult> = ArrayList()

        val codes = checkCodes.split("\n")

        val textErrors = checkTextErrors?.split("\n")

        val attributes = checkAttributeCodes?.split("\n")

        val values = checkAttributeValues?.split("\n")

        codes.indices.forEach { index ->
            pbResultList.add(
                PbResult(codes[index],
                textErrors?.let { if( textErrors.size > index)textErrors[index] else null},
                attributes?.let { if( attributes.size > index)attributes[index] else null},
                values?.let { if( values.size > index)values[index] else null})
            )
        }

        return pbResultList
    }
}