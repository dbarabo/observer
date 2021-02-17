package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.task.ActionTask
import java.time.LocalDate

enum class FileLoader(val actionTask: ActionTask, val prefixFile:String) {

    RPO(RpoLoader, "RPO"),

    ROO(RooLoader, "ROO"),

    ZSN(ZsnLoader, "ZSN"),

    ZSV(ZsvLoader, "ZSV"),

    APN(ApnLoader, "APN"),

    APZ(ApzLoader, "APZ"),

    APO(ApoLoader, "APO"),

    PNO(PnoLoader, "PNO"),

    IZV(Ticket440pCbr, "IZV"),

    KWT(Ticket440pFns, "KWT"),

    ZSO(zsvActionTask(),"ZSO");

    companion object {

        private val HASH_OBJECT_TYPES = values()
            .map { Pair(it.prefixFile, it.actionTask) }.toMap()

        fun objectByPrefix(prefix: String) :ActionTask? = HASH_OBJECT_TYPES[prefix]

    }
}

private fun zsvActionTask(): ActionTask {
    return if(!LocalDate.now().isBefore( LocalDate.of(2021, 3, 16))) ZsoLoaderVer4 else ZsoLoader
}