package ru.barabo.observer.config.skad.anywork.task.cbr.extract.loader

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader.XmlGutDfLoader
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader.xmlDateToTimestamp
import ru.barabo.observer.config.task.cbr.extract.request.MainFileRequest
import ru.barabo.observer.config.task.cbr.extract.request.RequestVkoListIdRequest
import java.io.File
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object CbrRequestLoader {

    fun loadRequest(file: File) {

        val mainRequest = loadRequestFromXml(file)

        val sessionSetting = AfinaQuery.uniqueSession()

        try {
            val idMain = createMainRequest(sessionSetting, mainRequest, file.name)

            for(listIdRequest in mainRequest.requestVko.requestVkoListIdRequest) {

                addListRequest(sessionSetting, idMain, listIdRequest)
            }

            AfinaQuery.commitFree(sessionSetting)
        } catch (e: Exception) {
            AfinaQuery.rollbackFree(sessionSetting)
        }

    }

    private fun addListRequest(sessionSetting: SessionSetting, idMain: Number, listIdRequest: RequestVkoListIdRequest) {

        for(period in listIdRequest.requestVkoPeriodList) {

            val params = arrayOf<Any?>(idMain,
                listIdRequest.clientValue,
                period.startPeriod.xmlDateToTimestamp(),
                period.endPeriod.xmlDateToTimestamp()
                )

            AfinaQuery.execute(INSERT_ITEM_REQUEST, params = params, sessionSetting = sessionSetting)
        }
    }

    private fun createMainRequest(sessionSetting: SessionSetting, mainRequest: MainFileRequest, filename: String): Number {
        val params = arrayOf<Any?>(filename.uppercase(Locale.getDefault()),
            mainRequest.guid,
            mainRequest.requestVko.regNumber,
            mainRequest.requestVko.startPeriod.xmlDateToTimestamp(),
            mainRequest.requestVko.endPeriod.xmlDateToTimestamp(),
            mainRequest.requestVko.typeRequest,
            mainRequest.requestVko.sentRequest.xmlDateTimeToTimestamp(),
            mainRequest.requestVko.idRequest,
            mainRequest.requestVko.dueResponse.xmlDateToTimestamp(),
            mainRequest.requestVko.isCardInfo.toNumberBoolean()
        )

        val (idMain) = AfinaQuery.execute(INSERT_MAIN_REQUEST, params = params,
            sessionSetting = sessionSetting, outParamTypes = intArrayOf(OracleTypes.NUMBER))!!

        return idMain as Number
    }

    private fun loadRequestFromXml(file: File): MainFileRequest {

        val xmlLoader = XmlCbrRequestLoader<MainFileRequest>()

        return xmlLoader.load(file)
    }
}

private const val INSERT_MAIN_REQUEST = "{ call OD.PTKB_CBR_EXTRACT.addMainRequest(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"

private const val INSERT_ITEM_REQUEST = "{ call OD.PTKB_CBR_EXTRACT.addItemRequest(?, ?, ?, ?) }"

fun String.toNumberBoolean() = if(this.uppercase(Locale.getDefault()) == "ДА") 1 else 0

fun String.xmlToLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))

fun String.xmlDateTimeToTimestamp(): Timestamp = xmlToLocalDateTime().toTimestamp()

fun LocalDateTime.toTimestamp(): Timestamp = Timestamp.valueOf(this)


