package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.task.p440.out.xml.extract.ExtractMainAccount

class AdditionalResponseData(
        val mainResponseData: ExtractMainResponseData,
        val account: ExtractMainAccount,
        val orderAccount :Int,
        val orderNumberFile :Int,
        val positionOperation :Int) : ResponseData {

    override fun typeInfo(): String = "ВЫПБНДОПОЛ"

    override fun xsdSchema(): String = "/xsd/BVD_300.xsd"
    override fun isSourceSmev(): Boolean = false

    override fun fileNameResponse(): String =
            String.format("${fileNameResponseTemplate()}.xml", AbstractResponseData.dateFormatInFile())

    override fun fileNameFromFns(): String = mainResponseData.fileNameFromFns()

    override fun fileNameResponseTemplate(): String {

        val template = mainResponseData.fileNameResponseTemplate().replace("BVS", "BVD")

        return "${template}_${String.format("%06d", orderAccount)}_${String.format("%06d",orderNumberFile)}"
    }

    override fun idFromFns(): Number = mainResponseData.idFromFns()

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}