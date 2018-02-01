package ru.barabo.observer.config.barabo.plastic.release.cycle

enum class StateRelease(val label: String, val dbValue: Int) {

    NEW("Новый", 0),
    OUT("Выпущен", 1),
    SENT("Отправлен", 2),
    SENT_OK("Отправка Ок", 3),
    SENT_ERROR("Отправка Error", 4),
    RESPONSE_OK_ALL("Ответ Всё Ок", 5),
    RESPONSE_OK_PART("Ответ Част Ок", 6),
    RESPONSE_ERROR_ALL("Error-Ответ Всё", 7),
    RESPONSE_ERROR_PART("Error-Ответ Част", 8),
    OCI_ALL("OCI-ВСЁ", 9),
    OCI_PART("OCI-Част", 10),
    SMS_SENT("SMS-Отправка", 11),
    SMS_SENT_ACCESS("SMS-Ok", 12),
    SMS_SENT_ERROR("SMS-Error", 13),
    SMS_RESPONSE_OK_ALL_OIA("SMS-Oтвет-Оk", 14),
    SMS_RESPONSE_OK_PART_OIA("SMS-Ответ Част Ок", 15),
    SMS_RESPONSE_ERROR_ALL_OIA("SMS-Oтвет-Error", 16),
    SMS_RESPONSE_ERROR_PART_OIA("SMS-Oтвет-Error Част", 17),
    CARD_GO("Карты в ГО", 18),
    CARD_SENT_OFFICES("Ушли в доп.офисы", 19),
    CARD_HOME_OFFICES("Карты в Доп. офисах", 20),
    CARD_TO_CLIENT("Выдано клиенту", 21);

    companion object {

        val MAX = CARD_TO_CLIENT

        fun stateByDbValue(dbValue: Int): StateRelease? = StateRelease.values().firstOrNull { it.dbValue == dbValue }
    }

    fun isErrorState() = when(this) {
        SENT_ERROR, RESPONSE_ERROR_ALL, RESPONSE_ERROR_PART, SMS_SENT_ERROR,
        SMS_RESPONSE_ERROR_ALL_OIA, SMS_RESPONSE_ERROR_PART_OIA  -> true
        else -> false
    }

}