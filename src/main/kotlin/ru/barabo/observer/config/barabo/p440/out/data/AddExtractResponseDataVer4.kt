package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.ExtractMainAccountVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.OperationAccountVer4
import kotlin.math.min

class AddExtractResponseDataVer4 (
    private val mainResponseData: ExtractMainResponseDataVer4,
    val account: ExtractMainAccountVer4,
    val orderNumberFile: Int,
    private val positionOperation: Int) : ResponseData {

    override fun typeInfo(): String = "ВЫПБНДОПОЛ"

    override fun xsdSchema(): String = "/xsd/440-П_BVD.xsd"

    override fun fileNameResponse(): String =
        String.format("${fileNameResponseTemplate()}.xml", AbstractResponseData.dateFormatInFile())

    override fun fileNameFromFns(): String = mainResponseData.fileNameFromFns()

    override fun fileNameResponseTemplate(): String {

        val template = mainResponseData.fileNameResponseTemplate().replace("BVS", "BVD")

        return "${template}_${account.orderFile}_${String.format("%06d",orderNumberFile)}"
    }

    override fun idFromFns(): Number = mainResponseData.idFromFns()

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getOperations(): List<OperationAccountVer4> {

        val countOperation = min(MAX_OPERATION_COUNT_EXT_VER4, mainResponseData.operationDataAccount.size - positionOperation)

        val operations = ArrayList<OperationAccountVer4>(countOperation)

        for(index in 0 until countOperation) {
            operations[index] = createOperation(mainResponseData.operationDataAccount[positionOperation + index])
        }

        return operations
    }

    private fun createOperation(row: Array<Any?>): OperationAccountVer4 {
        return OperationAccountVer4(
          //  row[]
        )
    }
}