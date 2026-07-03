package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.task.ActionTask

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

    ZSO(ZsoLoaderVer4,"ZSO"),

    ZSV(ZsvLoaderVer4, "ZSV"),

    UPO(UpoLoader, "UPO"),

    UNO(UnoLoader, "UNO"),

    UPV(UpvLoader, "UPV");


    companion object {

        private val HASH_OBJECT_TYPES = values().associate { Pair(it.prefixFile, it.actionTask) }

        fun objectByPrefix(prefix: String): ActionTask? = HASH_OBJECT_TYPES[prefix]
    }
}

