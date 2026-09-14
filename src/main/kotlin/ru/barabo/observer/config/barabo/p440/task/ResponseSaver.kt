package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseDataVer4
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseDataVer4
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseDataVer4
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseDataVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.bnp.FileBnpXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.FileExistsXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.FileExtractMainXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.rest.FileRestXmlVer4


object ExtractMainSaverVer4 : GeneralCreator<FileExtractMainXmlVer4>(ExtractMainResponseDataVer4(), FileExtractMainXmlVer4::class) {

    override fun name(): String = "Выгрузка выписки ver.4"
}

object RestSaverVer4 : GeneralCreator<FileRestXmlVer4>(RestResponseDataVer4(), FileRestXmlVer4::class) {
    override fun name(): String = "Выгрузка остатков ver.4"
}

object ExistsSaverVer4 : GeneralCreator<FileExistsXmlVer4>(ExistsResponseDataVer4(), FileExistsXmlVer4::class) {
    override fun name(): String = "Выгрузка наличие счетов ver.4"
}

object BnpSaverVer4 : GeneralCreator<FileBnpXmlVer4>(BnpResponseDataVer4(), FileBnpXmlVer4::class) {
    override fun name(): String = "Выгрузка BNP-файла ver.4"
}