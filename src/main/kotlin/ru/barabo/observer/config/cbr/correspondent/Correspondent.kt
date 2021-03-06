package ru.barabo.observer.config.cbr.correspondent

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.correspondent.task.DownLoadToCorrespond
import ru.barabo.observer.config.cbr.correspondent.task.SleepTest

object Correspondent : AbstractConfig() {

    override fun timeOut(): Long = 15_000

    override fun name() :String = "Корр. Счёт"

    override fun configRun() {

        DownLoadToCorrespond.findAll()
        //UploadFromCorrespond.findAll()

        SleepTest.findAll()

        executeTasks()
    }

}