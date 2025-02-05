package ru.barabo.observer.config.barabo.p440.out.data

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.ExtractMainAccountVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add.OperationAccountVer4
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class AddExtractResponseDataVer4 (
    private val mainResponseData: ExtractResponseData,
    val account: ExtractMainAccountVer4,
    val orderNumberFile: Int,
    positionOperation: Int,
    operationDataAccount: List<Array<Any?>>) : ResponseData {

    private val _operations: MutableList<OperationAccountVer4> = ArrayList()

    init {
        val countOperation = min(mainResponseData.maxOperationsCountInPart() + 1, operationDataAccount.size - positionOperation)

        for(index in 1 until countOperation) {
            _operations += createOperation(operationDataAccount[positionOperation + index], index)
        }
    }

    override fun typeInfo(): String = "ВЫПБНДОПОЛ"

    override fun xsdSchema(): String = "/xsd/440-П_BVD.xsd"
    override fun isSourceSmev(): Boolean = false

    override fun fileNameResponse(): String =
        String.format("${fileNameResponseTemplate()}.xml", dateFormatInFile())

    override fun fileNameFromFns(): String = mainResponseData.fileNameFromFns()

    override fun fileNameResponseTemplate(): String {

        val template = mainResponseData.fileNameResponseTemplate().replace("BVS", "BVD")

        return "${template}_${account.orderFile}_${String.format("%06d",orderNumberFile)}"
    }

    override fun idFromFns(): Number = mainResponseData.idFromFns()

    override fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getOperations(): List<OperationAccountVer4> = _operations

    private fun createOperation(row: Array<Any?>, orderOperation: Int): OperationAccountVer4 {
        return OperationAccountVer4(
            orderOperation,
            Timestamp((row[0] as Date).time),
            (row[12] as? Number) ?: 0.0,
            (row[13] as? Number) ?: 0.0,

            row[1] as? String,

            row[2] as? String,
            row[3] as? String,
            row[4] as? Date,

            row[5] as? String,
            row[6] as? String,
            row[7] as String,

            row[8] as? String,
            row[9] as? String ?: "0",
            row[10] as? String ?: "0",
            row[11] as? String
        )
    }
}