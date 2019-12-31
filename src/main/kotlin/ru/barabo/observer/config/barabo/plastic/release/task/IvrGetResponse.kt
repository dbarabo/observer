package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.template.file.FileProcessor
import java.io.File
import java.time.LocalTime
import java.util.regex.Pattern

object IvrGetResponse: FileFinder, FileProcessor {

    override fun name(): String = "ПИН-код: Получить ответ"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, workTimeFrom = LocalTime.of(7, 0))

    private const val IVR_RESPONSE_PATH = "\\\\jzdo/c$/quasionline-1.0.0/files/response"

    override val fileFinderData: List<FileFinderData> = listOf(FileFinderData(IVR_RESPONSE_PATH,"ivr_\\d{14}_\\d{4}\\.xml"))

    override fun processFile(file: File) {

        val filehCardIn = File("${LoadRestAccount.hCardInToday()}/${file.name}")

        file.copyTo(filehCardIn, true)

        val text = filehCardIn.readText()

        val matcherStatus = Pattern.compile("status(.*?)[<|lifeTime]").matcher(text)

        val status = if(matcherStatus.find()) matcherStatus.group(1).digitalOnly() else ""

        val matcherTime = Pattern.compile("lifeTime(.*?)[<|servicePhone]").matcher(text)

        val time = if(matcherTime.find()) matcherTime.group(1).digitalOnly() else ""

        val matcherPhone = Pattern.compile("servicePhone(.*?)[<|response]").matcher(text)

        val phone = if(matcherPhone.find()) matcherPhone.group(1).digitalOnly() else ""

        val statusInt = try {status.toInt()} catch (e :Exception) { -1 }

        updateStatusRequest(statusInt, time, phone, file)

        file.delete()
    }

    private fun updateStatusRequest(status: Int, time: String, phone: String, file: File) {
        if(status == 0) {
            AfinaQuery.execute(UPDATE_SUCCESS_IVR, arrayOf(time, phone, file.name.toUpperCase()))
        } else {

            val error = ERROR_CODE[status] ?: "Прочие ошибки"

            AfinaQuery.execute(UPDATE_ERROR_IVR, arrayOf(status, error, file.name.toUpperCase()))
        }
    }

    private val UPDATE_SUCCESS_IVR =
            "update od.PTKB_IVR_REGISTER set state = 1, WAIT_MINUTE = ?, SERVICE_PHONE = ? where UPPER(FILE_NAME) = ?"

    private val UPDATE_ERROR_IVR =
            "update od.PTKB_IVR_REGISTER set state = ?, ERROR_RESPONSE = ? where UPPER(FILE_NAME) = ?"

    private val ERROR_CODE = mapOf(
            -1 to "Прочие ошибки",
            11 to "Незарегистрированный идентификатор вызывающей системы",
            12 to "Неподдерживаемый тип идентификатора карты",
            13 to "Плохой идентификатор карты",
            14 to "Нет такой карты",
            15 to "Плохой номер телефона",
            16 to "Плохой статус карты",
            17 to "Плохой сертификат или сертификат отсутствует",
            91 to "Сервис временно недоступен",
            99 to "Неопределенная внутренняя ошибка сервера"
    )
}

private fun String.digitalOnly() = this.replace("\\D+".toRegex(), "")