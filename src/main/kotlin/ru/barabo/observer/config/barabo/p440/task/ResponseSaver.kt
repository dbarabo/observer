package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.data.*
import ru.barabo.observer.config.task.p440.out.xml.bnp.BnpXml
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistXml
import ru.barabo.observer.config.task.p440.out.xml.extract.ExtractMainXml
import ru.barabo.observer.config.task.p440.out.xml.rest.RestXml
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.FileExistsXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.FileExtractMainXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.rest.FileRestXmlVer4


object BnpSaver : GeneralCreator<BnpXml>(BnpResponseData(), BnpXml::class) {
    override fun name(): String = "Выгрузка BNP-файла"
}

object ExistsSaver : GeneralCreator<ExistXml>(ExistsResponseData(), ExistXml::class) {
    override fun name(): String = "Выгрузка наличия счетов"
}

object RestSaver : GeneralCreator<RestXml>(RestResponseData(), RestXml::class) {
    override fun name(): String = "Выгрузка остатков счетов"
}

object ExtractMainSaver : GeneralCreator<ExtractMainXml>(ExtractMainResponseData(), ExtractMainXml::class) {

    override fun name(): String = "Выгрузка выписки"
}

object ExtractMainSaverVer4 : GeneralCreator<FileExtractMainXmlVer4>(ExtractMainResponseDataVer4(), FileExtractMainXmlVer4::class) {

    override fun name(): String = "Выгрузка выписки ver.4"
}

object RestSaverVer4 : GeneralCreator<FileRestXmlVer4>(RestResponseDataVer4(), FileRestXmlVer4::class) {
    override fun name(): String = "Выгрузка остатков ver.4"
}

object ExistsSaverVer4 : GeneralCreator<FileExistsXmlVer4>(ExistsResponseDataVer4(), FileExistsXmlVer4::class) {
    override fun name(): String = "Выгрузка наличие счетов  ver.4"
}