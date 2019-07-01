package ru.barabo.observer.store

import ru.barabo.db.SessionException
import ru.barabo.observer.afina.AfinaConnect
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.barabo.p440.P440Config
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.cbr.ibank.IBank
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.test.TestConcurrencyConfig
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.util.*
import kotlin.concurrent.timer

object TaskMapper {

    private var configList :List<ConfigTask> = emptyList()

    private var build: String = ""

    private var isAfina: Boolean = false

    var masterPswd: String = "K.,jqRf;lsq"
    private set

    fun objectByClass(clazzName :String) :ActionTask {
        val clazz = Class.forName(clazzName).kotlin

        return (clazz.objectInstance ?: clazz.java.newInstance())as ActionTask
    }

    @Throws(SessionException::class)
    fun init(build :String, baseConnect :String) {

        if(this.build.isNotEmpty()) {
            throw SessionException("TaskMapper already initialized")
        }

        this.build = build

        this.configList =
                when (build.toUpperCase().trim()) {
            "CBR" -> { cbrConfigs() }
            "BARABO" -> { baraboConfigs() }
            "TEST" -> { testConfig() }
            else -> { throw SessionException("TaskMapper build name is unknown $build") }
        }

        val baseConnectReal = if(build == "TEST") "TEST" else baseConnect

        initBase(baseConnectReal)
    }

    private fun initBase(baseConnect: String) {
        if(baseConnect != "AFINA" && baseConnect != "TEST") {
            throw SessionException("baseConnect must be contains only AFINA or TEST values")
        }

        isAfina = baseConnect == "AFINA"

        AfinaConnect.init(isAfina)
    }

    fun isAfinaBase() = isAfina

    fun build() = build

    fun configList(): List<ConfigTask> = configList

    fun runConfigList() {
        if(configList.isEmpty()) {
            throw SessionException("TaskMapper is not initialized")
        }

        configList.forEach { it.starting() }

        startChecker()
    }

    fun stopConfigList() {
        stopingChecker()

        configList.forEach { it.stoping() }
    }

    private fun cbrConfigs(): List<ConfigTask> = listOf(IBank, /*Correspondent,*/ PtkPsd, TicketPtkPsd, OtherCbr)

    private fun baraboConfigs(): List<ConfigTask> = listOf(CryptoConfig, P440Config, PlasticTurnConfig, PlasticReleaseConfig)

    private fun testConfig(): List<ConfigTask> = listOf(TestConfig, TestConcurrencyConfig)

    private var timerChecker: Timer? = null

    private fun startChecker() {
        timerChecker = timer(name = build, initialDelay = 10_000, daemon = false, period = 60*10_000) {
            AfinaQuery.execute(updateBuild(build) )

            checkOtherBuilds()
        }
    }

    private fun checkOtherBuilds() {
        AfinaQuery.select(selectOtherBuilds(build) ).forEach {
            val buildName = it[0] as? String ?: "???"

            val lastWork = it[1] as Date

            val message = "Похоже приложение observer.jar с билдом $buildName не запущено и не отвечает с $lastWork"

            BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = message, body = message)

            AfinaQuery.execute(updateBuild(buildName) )
        }
    }

    private fun stopingChecker() {

        timerChecker?.cancel()
        timerChecker?.purge()

        timerChecker = null
    }
}

private fun updateBuild(build: String) =
        "update od.PTKB_VERSION_JAR set DUE = sysdate where PROGRAM = 'OBSERVER.JAR' and BUILD = '$build'"

private fun selectOtherBuilds(build: String) =
        "select BUILD, DUE from od.PTKB_VERSION_JAR where PROGRAM = 'OBSERVER.JAR' and coalesce(BUILD, '!') != '$build' and (sysdate - DUE > 40/(60*24) )"