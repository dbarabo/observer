package ru.barabo.observer.main

import org.slf4j.LoggerFactory
import ru.barabo.observer.gui.startLaunch
import ru.barabo.observer.store.TaskMapper

object Observer {
    val logger = LoggerFactory.getLogger(Observer::class.java)!!

    fun startConfigs(args:Array<String>) {

        TaskMapper.init("CBR", "TEST"/*"AFINA"*/)

        logger.info("run ConfigList")
        TaskMapper.runConfigList()

        logger.info("start Launch")
        startLaunch(args)

    }
}

fun main(args:Array<String>) {

    Observer.startConfigs(args)
}
