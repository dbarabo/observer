package ru.barabo.observer.main

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.gui.startLaunch
import ru.barabo.observer.store.TaskMapper
import java.net.InetAddress
import java.util.*

object Observer {
    val logger = LoggerFactory.getLogger(Observer::class.java)!!

    fun startConfigs(args: Array<String>) {

        val comp = InetAddress.getLocalHost().hostName.uppercase(Locale.getDefault())

        val config = when (comp) {
            "BARABO" -> "BARABO"
            "OPERATOR" -> "CBR"
            "JZDO" -> "JZDO"
            "SKAD" -> "SCAD"
            "BARABO_" -> "TEST"
            "DSPO" -> "TEST"
            "POSEIDON" -> "CORRESPOND"
            "KBRN" -> "CORRESPOND"

            "FNS" -> "ENSSIGN"

            else -> throw Exception("Неизвестная конфигурация для компа $comp")
        }

        TaskMapper.init(config, /*if(config == "ENSSIGN")"TEST" else*/"AFINA")

        TaskMapper.runConfigList()

        logger.info("start Launch")
        startLaunch(args)

    }
}

fun main(args:Array<String>) {

   Observer.startConfigs(args)
}
