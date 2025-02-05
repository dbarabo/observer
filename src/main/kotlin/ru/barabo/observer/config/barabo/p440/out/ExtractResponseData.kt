package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.ExtractMainAccountVer4

interface ExtractResponseData : RequestResponseData {

    fun getAccountAbsentList(): List<AccountAbsent>

    fun getMainAccountList(): List<ExtractMainAccountVer4>

    fun maxOperationsCountInPart(): Int
}