package ru.barabo.observer.config.cbr.ibank

import ru.barabo.observer.config.AbstractConfig
import ru.barabo.observer.config.cbr.ibank.task.CheckAccountFaktura
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoCreator
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoSaver
import ru.barabo.observer.config.cbr.ibank.task.UploadExtract

object IBank : AbstractConfig() {

    override fun timeOut(): Long = 60_000

    override fun name() :String = "Выгрузки в Интернет-Банк"

    override fun configRun() {

        UploadExtract.findAll()
        CheckAccountFaktura.findAll()

        LoanInfoCreator.findAll()
        LoanInfoSaver.findAll()

        executeTasks()
    }
}