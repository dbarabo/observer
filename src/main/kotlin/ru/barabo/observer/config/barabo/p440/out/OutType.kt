package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.skad.crypto.task.PbSaverScad

enum class OutType(val dbValue :Int, val outObject :GeneralCreator<*>?) {

    NONE(0, null),
    PB(1, PbSaverScad),
    REST(2, RestSaver),
    EXTRACT(3, ExtractMainSaver),
    EXISTS(4, ExistsSaver),
    EXTRACT_ADDITIONAL(5, null),
    BNP(6, BnpSaver);

    companion object {
        private val HASH_DBVALUE_CREATOR = OutType.values().map { it -> Pair(it.dbValue, it.outObject) }.toMap()

        fun creatorByDbValue(value :Int) :GeneralCreator<*>? = HASH_DBVALUE_CREATOR[value]
    }
}