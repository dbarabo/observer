package ru.barabo.observer.config.cbr.ibank

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.ibank.task.CheckAccountFaktura
import ru.barabo.observer.config.cbr.ibank.task.UploadExtract
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerAllBalance

object IBank : AbstractConfig() {

    override fun timeOut(): Long = 60_000

    override fun name() :String = "Выгрузки в Интернет-Банк"

    override fun configRun() {

        UploadExtract.findAll()
        CheckAccountFaktura.findAll()

        // off from 01/01/2021
        // LoanInfoCreator.findAll()
        // LoanInfoSaver.findAll()

        CheckerAllBalance.findAll()

        executeTasks()
    }
}