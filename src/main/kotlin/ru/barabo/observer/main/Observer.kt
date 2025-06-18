package ru.barabo.observer.main

import org.slf4j.LoggerFactory
import ru.barabo.observer.gui.startLaunch
import ru.barabo.observer.store.TaskMapper
import java.net.InetAddress
import java.net.ServerSocket
import java.util.*
import kotlin.system.exitProcess

object Observer {
    val logger = LoggerFactory.getLogger(Observer::class.java)!!

    private lateinit var lock: ServerSocket

    fun startConfigs(args: Array<String>) {

        try {
            lock = ServerSocket(2905)
        } catch (e: java.lang.Exception) {
            logger.error("Observer already running! Exit...")
            exitProcess(-1)
        }
        logger.error("ServerSocket 2905 created")

        val comp = InetAddress.getLocalHost().hostName.uppercase(Locale.getDefault())

        val config = when (comp) {
            "GRYPTOPRO"-> "GRYPTOPRO"
            "OPERATOR" -> "OPERATOR"
            "JZDO" -> "JZDO"
            "DSPO" -> "TEST"
            "KBRN" -> "KBRN"

            "FNS" -> "FNS"

            else -> throw Exception("Неизвестная конфигурация для компа $comp")
        }

        TaskMapper.init(config, "AFINA")

        TaskMapper.runConfigList()

        logger.info("start Launch")
        startLaunch(args)

    }
}

fun main(args:Array<String>) {

   Observer.startConfigs(args)
}
