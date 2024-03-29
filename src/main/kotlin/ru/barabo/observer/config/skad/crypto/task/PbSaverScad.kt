package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseData
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseDataVer4
import ru.barabo.observer.config.barabo.p440.out.sendFolder440pSmev
import ru.barabo.observer.config.task.p440.out.xml.pb.PbXml
import ru.barabo.observer.config.task.p440.out.xml.ver4.pb.FilePbXmlVer4
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File

object PbSaverScad : GeneralCreator<PbXml>(PbResponseData(), PbXml::class) {

    override fun name(): String = "440-П Выгрузка PB-файла SCAD OLD"

    override fun execute(elem: Elem): State {
        val result = super.execute(elem)

        val sourcePb = File("${sendFolder440p().absolutePath}/${responseData.fileNameResponse()}")

        ScadComplex.signAndMoveSource(sourcePb, sourceFolder())

        return result
    }
}

object PbSaverScadVer4 : GeneralCreator<FilePbXmlVer4>(PbResponseDataVer4(), FilePbXmlVer4::class) {

    override fun name(): String = "440-П Выгрузка PB-файла SCAD"

    override fun execute(elem: Elem): State {
        val result = super.execute(elem)

        val pbFolder = if(responseData.isSourceSmev()) sendFolder440pSmev().absolutePath else sendFolder440p().absolutePath

        val sourceFolderThis = if(responseData.isSourceSmev()) sourceFolderSmev() else sourceFolder()

        val sourcePb = File("$pbFolder/${responseData.fileNameResponse()}")

        ScadComplex.signAndMoveSource(sourcePb, sourceFolderThis)

        return result
    }
}

fun sourceFolder() = "${GeneralCreator.sendFolder440p().absolutePath}/src".byFolderExists()

fun sourceFolderSmev() = "${sourceFolder().absolutePath}/smev".byFolderExists()