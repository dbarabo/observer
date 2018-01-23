package ru.barabo.observer.store

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaConnect
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.correspondent.Correspondent
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.ActionTask

object TaskMapper {

    private var mapper :Map<String, ActionTask> = emptyMap()

    private var configList :List<ConfigTask> = emptyList()

    private var build :String = ""

    private var isAfina :Boolean = false

    fun objectByClass(clazzName :String) :ActionTask { // = mapper[clazz]!!
        val clazz = Class.forName(clazzName).kotlin

        return (clazz.objectInstance ?: clazz.java.newInstance())as ActionTask
    }

    @Throws(SessionException::class)
    fun init(build :String, baseConnect :String) {

        this.build = build

        if(!mapper.isEmpty()) {
            throw SessionException("TaskMapper already initialized")
        }

        val (mapper, configList) =
                when (build.toUpperCase().trim()) {
            "CBR" -> {
                Pair(initCbr(), cbrConfigs())
            }
            "BARABO" -> {
                Pair(initBarabo(), baraboConfigs())
            }
            else -> {
                Pair(emptyMap(), emptyList())
            }
        }

        this.mapper = mapper
        this.configList = configList

        initBase(baseConnect)
    }

    private fun initBase(baseConnect :String) {
        if(baseConnect != "AFINA" && baseConnect != "TEST") {
            throw SessionException("baseConnect must be contains only AFINA or TEST values")
        }

        isAfina = baseConnect =="AFINA"

        AfinaConnect.init(isAfina)
    }

    fun isAfinaBase() = isAfina

    fun build() = build

    fun configList() :List<ConfigTask> = configList

    fun runConfigList() {
        if(configList.isEmpty()) {
            throw SessionException("TaskMapper is not initialized")
        }

        configList.forEach { it.starting() }
    }

    fun stopConfigList() = configList.forEach { it.stoping() }

    private fun cbrConfigs() :List<ConfigTask> = listOf(Correspondent, PtkPsd, TicketPtkPsd, OtherCbr)

    private fun initCbr() :Map<String, ActionTask> = emptyMap()
    // {
//        return mapOf(
//                mapItem(DownLoadToCorrespond),
//                mapItem(UploadFromCorrespond),
//
//                mapItem(Get440pFiles),
//                mapItem(Send364pSign),
//                mapItem(SendByPtkPsdCopy),
//                mapItem(SendByPtkPsdNoXml),
//                mapItem(SendXmlByPtkbPsd),
//                mapItem(CheckerIsSendPtkPsd),
//                mapItem(GetProcess550pFiles),
//                mapItem(Send440pArchive),
//
//                mapItem(RemartMail),
//                mapItem(ResponseToOrderCbr),
//                mapItem(SbMailFromCbr),
//                mapItem(TtsMailFromOtk),
//                mapItem(NbkiAllReportsSend),
//                mapItem(UnlockUsersMonday),
//                mapItem(CecReportProcess),
//
//                mapItem(Fsfm349pRequest),
//                mapItem(Ticket311pCbr),
//                mapItem(Ticket311pFns),
//                mapItem(Ticket364FtsCab),
//                mapItem(Ticket364FtsText),
//                mapItem(Ticket550p),
//                mapItem(TicketFsfm349p),
//                mapItem(TicketFtsCab),
//                mapItem(TicketFtsText),
//                mapItem(TicketLegalCab),
//                mapItem(TicketLegalText),
//                mapItem(TicketSimple),
//                mapItem(TicketXml),
//                mapItem(TicketVbkArchive)
//                )
//    }

    private fun baraboConfigs() :List<ConfigTask> = emptyList()

    private fun initBarabo() :Map<String, ActionTask> = emptyMap()

    private fun mapItem(objTask :ActionTask) :Pair<String, ActionTask> = Pair(objTask.javaClass.canonicalName, objTask)
}