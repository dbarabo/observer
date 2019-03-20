package ru.barabo.observer.config.barabo.plastic.release.cycle

enum class TypePacket(val label: String, val dbValue: Int) {
    REISSUE("Перевыпуск", 0),
    RELEASE("Выпуск", 1),
    SMS_ADD("SMS-Подключить", 2),
    SMS_REMOVE("SMS-Отключить", 3),
    BTRT25("BTRT25 Смена продукта", 4),
    UNNAMED("Неименные карты", 5),
    BTRT30("Смена персональных данных", 6),
    BTRT15_SUSPEND("Блокировать счет полностью", 7),
    BTRT15_ACTIVE("Разлокировать счет", 8);

    companion object {
        fun getTypePacketByDbValue(dbValue: Int): TypePacket? = TypePacket.values().firstOrNull { it.dbValue == dbValue }
    }
}