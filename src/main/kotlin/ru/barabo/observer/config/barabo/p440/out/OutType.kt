package ru.barabo.observer.config.barabo.p440.out

enum class OutType(val dbValue :Int) {

    NONE(0),
    PB(1),
    REST(2),
    EXTRACT(3),
    EXISTS(4),
    EXTRACT_ADDITIONAL(5),
    BNP(6)
}