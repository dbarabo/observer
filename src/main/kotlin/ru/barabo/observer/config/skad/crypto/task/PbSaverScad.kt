package ru.barabo.observer.config.skad.crypto.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.ResponseData
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

        val sourcePb = File("$pbFolder/${responseData.fileNameResponse()}")

        processFilePbNewSmev(sourcePb, responseData)

        return result
    }
}

private fun processFilePbNewSmev(sourcePb: File, responseData: ResponseData) {

    val smevTypeFile = (AfinaQuery.selectValue(SELECT_SMEV_TYPE_FILE, arrayOf(sourcePb.name)) as Number).toInt()

    when(smevTypeFile) {
        OLD_VERSION_FILES, PB_OLD_SMEV_FILES -> cryptoScadPb(sourcePb, responseData)

        NEW_SMEV_440P_FILES -> setFileToNew440p(sourcePb)

        NEW_SMEV_UNO_373_FILES -> setFileToNewSmev403(sourcePb)

        else -> throw Exception("Неизвестный источник файла smevTypeFile=$smevTypeFile sourcePb=$sourcePb")
    }
}

private fun cryptoScadPb(sourcePb: File, responseData: ResponseData) {
    val sourceFolderThis = if(responseData.isSourceSmev()) sourceFolderSmev() else sourceFolder()

    ScadComplex.signAndMoveSource(sourcePb, sourceFolderThis)
}

fun sourceFolder() = "${GeneralCreator.sendFolder440p().absolutePath}/src".byFolderExists()

fun sourceFolderSmev() = "${sourceFolder().absolutePath}/smev".byFolderExists()