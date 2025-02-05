package ru.barabo.observer.config.fns.cbr.extract

import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.barabo.p440.out.data.generatePbResult
import ru.barabo.observer.config.task.p440.out.xml.pb.PbResult

class PbResponseDataCbr : ResponseData {

    private lateinit var idCbrRequest: Number

    private lateinit var filenameResponse: String

    private lateinit var filenameRequest: String

    lateinit var pbResult : List<PbResult>
        private set

    override fun idFromFns(): Number = idCbrRequest

    override fun typeInfo(): String = "ПОДБНПРИНТ"

    override fun xsdSchema(): String = "/xsd/cbr/PB.xsd"

    override fun fileNameResponse(): String = filenameResponse

    override fun fileNameFromFns(): String = filenameRequest

    override fun fileNameResponseTemplate(): String = ""

    override fun isSourceSmev(): Boolean = false

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {

        val (idCbr, responseFile, requestFile, checkCodes, checkAttributeCodesAndValues) =
             AfinaQuery.selectCursor(SEL_CBR_PB_RESPONSE, arrayOf(idResponse), sessionSetting = sessionSetting)[0]

        idCbrRequest = idCbr as Number

        filenameResponse = responseFile as String

        filenameRequest = requestFile  as String

        val checkAttributeCodes = checkAttributeCodesAndValues?.toString()?.split("|")

        val checkText = checkAttributeCodes?.get(0)?.takeIf { it.isNotEmpty() }

        val checkAttributes = checkAttributeCodes?.get(1)?.takeIf { it.isNotEmpty() }

        val checkAttributeValues = checkAttributeCodes?.get(2)?.takeIf { it.isNotEmpty() }

        pbResult = generatePbResult(checkCodes as String, checkText, checkAttributes, checkAttributeValues)

        return this
    }
}

private const val SEL_CBR_PB_RESPONSE = "{ ? = call OD.PTKB_CBR_EXTRACT.getPBInfoResponseData( ? ) }"