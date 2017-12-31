package ru.barabo.observer.config.cbr.correspondent

import org.slf4j.LoggerFactory
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.ExitStatus
import ru.barabo.observer.config.cbr.correspondent.task.DownLoadToCorrespond
import ru.barabo.observer.config.cbr.correspondent.task.UploadFromCorrespond

object Correspondent : ConfigTask {

    override var exitStatus: ExitStatus = ExitStatus.STOP

    val logger = LoggerFactory.getLogger(Correspondent::class.java)!!

    override fun timeOut(): Long = 10_000

    override fun name() :String = "Корр. Счёт"

    override fun configRun() {

        DownLoadToCorrespond.findAll()

        UploadFromCorrespond.findAll()

        DownLoadToCorrespond.executeAll()

        UploadFromCorrespond.executeAll()
    }

}