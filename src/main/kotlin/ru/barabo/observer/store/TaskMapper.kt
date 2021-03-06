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
import ru.barabo.observer.config.cbr.sender.SenderMail
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.cbr.turncard.TurnCard
import ru.barabo.observer.config.jzdo.upay.UPayConfig
import ru.barabo.observer.config.skad.acquiring.Acquiring
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.mail.smtp.BaraboSmtp
import java.util.*

import kotlin.concurrent.*

object TaskMapper {

    lateinit var buildInfo: BuildInfo
    private set

    private var isAfina: Boolean = false

    var masterPswd: String = "K.,jqRf;lsq"
    private set

    fun objectByClass(clazzName: String): ActionTask {

            val clazz = Class.forName(clazzName).kotlin

            return (clazz.objectInstance ?: clazz.java.newInstance()) as ActionTask
    }

    @Throws(SessionException::class)
    fun init(build :String, baseConnect :String) {

        if(::buildInfo.isInitialized) {
            throw SessionException("TaskMapper already initialized")
        }

        buildInfo = getBuildInfoByBuild(build)

        val baseConnectReal = baseConnect // if(buildInfo.build == "TEST") "TEST" else baseConnect

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

    fun runConfigList() {
        buildInfo.configs.forEach { it.starting() }

        startChecker()
    }

    fun stopConfigList() {
        stopingChecker()

        buildInfo.configs.forEach { it.stoping() }
    }

    private var timerChecker: Timer? = null

    private fun startChecker() {
        timerChecker = timer(name = buildInfo.build, initialDelay = 10_000, daemon = false, period = 60*10_000) {
            AfinaQuery.execute(updateBuild(buildInfo.build) )

            checkOtherBuilds()
        }
    }

    private fun checkOtherBuilds() {
        AfinaQuery.select(selectOtherBuilds(buildInfo.build) ).forEach {
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

enum class BuildInfo(val build: String, val configs: List<ConfigTask>) {
    Cbr("CBR", cbrConfigs()),
    Barabo("BARABO", baraboConfigs()),
    Jzdo("JZDO", jzdoConfigs()),
    Scad("SCAD", scadSignatureConfigs()),
    Test("TEST", testConfig())
}

private fun getBuildInfoByBuild(build: String): BuildInfo =
        BuildInfo.values().firstOrNull { it.build == build } ?: throw SessionException("build name is unknown $build")

private fun cbrConfigs(): List<ConfigTask> = listOf(IBank, PtkPsd, TicketPtkPsd, OtherCbr, SenderMail)

private fun jzdoConfigs(): List<ConfigTask> = listOf(UPayConfig, PlasticTurnConfig, PlasticReleaseConfig)

private fun scadSignatureConfigs(): List<ConfigTask> = listOf(PlasticOutSide, ScadConfig, P440Config, Acquiring)

private fun baraboConfigs(): List<ConfigTask> = listOf(CryptoConfig)

private fun testConfig(): List<ConfigTask> = listOf( TestConfig )
