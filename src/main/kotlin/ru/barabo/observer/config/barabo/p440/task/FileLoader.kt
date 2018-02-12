package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.task.ActionTask

enum class FileLoader(val actionTask : ActionTask, val prefixFile :String) {

    RPO(RpoLoader, "RPO"),

    ROO(RooLoader, "ROO"),

    ZSN(ZsnLoader, "ZSN"),

    ZSO(ZsoLoader, "ZSO"),

    ZSV(ZsvLoader, "ZSV"),

    APN(ApnLoader, "APN"),

    APZ(ApzLoader, "APZ"),

    APO(ApoLoader, "APO"),

    PNO(PnoLoader, "PNO"),

    IZV(Ticket440pCbr, "IZV"),

    KWT(Ticket440pFns, "KWT");

    companion object {

        private val HASH_OBJECT_TYPES = FileLoader.values().map { Pair(it.prefixFile, it.actionTask) }.toMap()

        fun objectByPrefix(prefix: String) :ActionTask? = HASH_OBJECT_TYPES[prefix]
    }
}