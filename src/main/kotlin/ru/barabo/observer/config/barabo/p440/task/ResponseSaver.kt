package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.data.*
import ru.barabo.observer.config.task.p440.out.xml.bnp.BnpXml
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistXml
import ru.barabo.observer.config.task.p440.out.xml.extract.ExtractMainXml
import ru.barabo.observer.config.task.p440.out.xml.pb.PbXml
import ru.barabo.observer.config.task.p440.out.xml.rest.RestXml
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File


object PbSaver : GeneralCreator<PbXml>(PbResponseData(), PbXml::class) {

    override fun name(): String = "Выгрузка PB-файла"

    override fun execute(elem: Elem): State {
        val result = super.execute(elem)

        val signFile = File("${sendFolder440p().absolutePath}/${responseData.fileNameResponse()}")

        Verba.signByBarabo(signFile)

        return result
    }
}

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

object PbSaverScad : GeneralCreator<PbXml>(PbResponseData(), PbXml::class) {

    override fun name(): String = "Выгрузка PB-файла SCAD"

    override fun execute(elem: Elem): State {
        val result = super.execute(elem)

        val signFile = File("${sendFolder440p().absolutePath}/${responseData.fileNameResponse()}")

        Verba.signByBarabo(signFile)

        return result
    }
}

