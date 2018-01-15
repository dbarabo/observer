package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml
import java.util.*


interface RequestResponseData :ResponseData {
    /**
     * @return Номер запроса налогового органа
     */
    fun getNumberRequest(): String

    /**
     * @return дата запроса налогового органа
     */
    fun getDateRequest(): Date

    /**
     * @return Наименование файла PB
     */
    fun getPbFileName(): String

    fun getPayer(): PayerXml

    fun getFns(): FnsXml

    /**
     * @return вид справки берем из базы 7-Справка об остатках денежных средств
     * на вкладах (депозитах) 2-Справка об остатках денежных средств на
     * счетах (специальных банковских счетах) 4-Справка об остатках
     * электронных денежных средств
     */
    fun getViewHelp(): String
}