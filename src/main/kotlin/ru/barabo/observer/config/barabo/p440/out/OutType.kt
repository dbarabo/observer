package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.skad.crypto.task.PbSaverScad
import ru.barabo.observer.config.skad.crypto.task.PbSaverScadVer4

enum class OutType(val dbValue :Int, val outObject :GeneralCreator<*>?) {

    NONE(0, null),
    PB(1, pbCreator() ),
    REST(2, restMainCreator() ),
    EXTRACT(3, extractMainCreator() ),
    EXISTS(4, existsMainCreator() ),
    EXTRACT_ADDITIONAL(5, null),
    BNP(6, BnpSaver);

    companion object {
        private val HASH_DBVALUE_CREATOR = values().map { it -> Pair(it.dbValue, it.outObject) }.toMap()

        fun creatorByDbValue(value :Int) :GeneralCreator<*>? = HASH_DBVALUE_CREATOR[value]
    }
}

private fun extractMainCreator(): GeneralCreator<*> = if( isNewFormat2021() ) ExtractMainSaverVer4 else ExtractMainSaver

private fun restMainCreator(): GeneralCreator<*> = if( isNewFormat2021() ) RestSaverVer4 else RestSaver

private fun existsMainCreator(): GeneralCreator<*> = if( isNewFormat2021() ) ExistsSaverVer4 else ExistsSaver

private fun pbCreator(): GeneralCreator<*> = if( isNewFormat2021() ) PbSaverScadVer4 else PbSaverScad