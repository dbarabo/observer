package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.skad.crypto.task.PbSaverScadVer4

enum class OutType(val dbValue :Int, val outObject :GeneralCreator<*>?) {

    NONE(0, null),
    PB(1, PbSaverScadVer4 ),
    REST(2, RestSaverVer4 ),
    EXTRACT(3, ExtractMainSaverVer4 ),
    EXISTS(4, ExistsSaverVer4 ),
    EXTRACT_ADDITIONAL(5, null),
    BNP(6, BnpSaverVer4 );

    companion object {
        private val HASH_DBVALUE_CREATOR = values().associate { it -> Pair(it.dbValue, it.outObject) }

        fun creatorByDbValue(value :Int) :GeneralCreator<*>? = HASH_DBVALUE_CREATOR[value]
    }
}
