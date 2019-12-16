package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseData
import ru.barabo.observer.config.task.p440.out.xml.pb.PbXml
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File

object PbSaverScad : GeneralCreator<PbXml>(PbResponseData(), PbXml::class) {

    override fun name(): String = "440-П Выгрузка PB-файла SCAD"

    fun sourceFolder() = "${sendFolder440p().absolutePath}/src".byFolderExists()

    override fun execute(elem: Elem): State {
        val result = super.execute(elem)

        val sourcePb = File("${sendFolder440p().absolutePath}/${responseData.fileNameResponse()}")

        ScadComplex.signAndMoveSource(sourcePb, sourceFolder())

        return result
    }
}