package ru.barabo.observer.store

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaConnect
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.cbr.correspondent.Correspondent
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.ActionTask

object TaskMapper {

    private var configList :List<ConfigTask> = emptyList()

    private var build :String = ""

    private var isAfina :Boolean = false

    fun objectByClass(clazzName :String) :ActionTask {
        val clazz = Class.forName(clazzName).kotlin

        return (clazz.objectInstance ?: clazz.java.newInstance())as ActionTask
    }

    @Throws(SessionException::class)
    fun init(build :String, baseConnect :String) {

        if(!this.build.isEmpty()) {
            throw SessionException("TaskMapper already initialized")
        }

        this.build = build

        this.configList =
                when (build.toUpperCase().trim()) {
            "CBR" -> { cbrConfigs() }
            "BARABO" -> { baraboConfigs() }
            else -> { throw SessionException("TaskMapper build name is unknown $build") }
        }

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

    private fun baraboConfigs() :List<ConfigTask> = listOf(CryptoConfig, P440Config, PlasticTurnConfig, PlasticReleaseConfig)

    //private fun mapItem(objTask :ActionTask) :Pair<String, ActionTask> = Pair(objTask.javaClass.canonicalName, objTask)
}