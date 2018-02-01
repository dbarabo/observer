package ru.barabo.observer.main

import org.slf4j.LoggerFactory
import ru.barabo.observer.gui.startLaunch
import ru.barabo.observer.store.TaskMapper
import java.net.InetAddress

object Observer {
    val logger = LoggerFactory.getLogger(Observer::class.java)!!

    fun startConfigs(args:Array<String>) {

        val comp = InetAddress.getLocalHost().hostName.toUpperCase()

        val config = when(comp) {
            "BARABO" -> "BARABO"
            "OPERATOR" -> "CBR"
            else -> throw Exception("Неизвестная конфигурация для компа $comp")
        }

        TaskMapper.init(config, "AFINA")

        logger.info("run ConfigList")
        TaskMapper.runConfigList()

        logger.info("start Launch")
        startLaunch(args)

    }
}

fun main(args:Array<String>) {

    Observer.startConfigs(args)
}
