package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.task.ActionTask
import java.time.LocalDate

fun isNewFormat2021(): Boolean = !LocalDate.now().isBefore( LocalDate.of(2021, 3, 16))

private fun zsoActionTask(): ActionTask {
    return if(isNewFormat2021()) ZsoLoaderVer4 else ZsoLoader
}

private fun zsvActionTask(): ActionTask {
    return if(isNewFormat2021()) ZsvLoaderVer4 else ZsvLoader
}

enum class FileLoader(val actionTask: ActionTask, val prefixFile:String) {

    RPO(RpoLoader, "RPO"),

    ROO(RooLoader, "ROO"),

    ZSN(ZsnLoader, "ZSN"),

    APN(ApnLoader, "APN"),

    APZ(ApzLoader, "APZ"),

    APO(ApoLoader, "APO"),

    PNO(PnoLoader, "PNO"),

    IZV(Ticket440pCbr, "IZV"),

    KWT(Ticket440pFns, "KWT"),

    ZSO(zsoActionTask(),"ZSO"),

    ZSV(zsvActionTask(), "ZSV"),

    UPO(UpoLoader, "UPO");

    companion object {

        private val HASH_OBJECT_TYPES = values().associate { Pair(it.prefixFile, it.actionTask) }

        fun objectByPrefix(prefix: String): ActionTask? = HASH_OBJECT_TYPES[prefix]
    }
}

