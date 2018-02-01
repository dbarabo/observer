package ru.barabo.observer.config.cbr.correspondent

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.correspondent.task.DownLoadToCorrespond
import ru.barabo.observer.config.cbr.correspondent.task.UploadFromCorrespond
import java.util.*

object Correspondent : ConfigTask {

    override var timer: Timer? = null

    val logger = LoggerFactory.getLogger(Correspondent::class.java)!!

    override fun timeOut(): Long = 15_000

    override fun name() :String = "Корр. Счёт"

    override fun configRun() {

        DownLoadToCorrespond.findAll()
        UploadFromCorrespond.findAll()

        executeTasks()
    }

}