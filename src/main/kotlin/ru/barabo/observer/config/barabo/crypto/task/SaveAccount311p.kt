package ru.barabo.observer.config.barabo.crypto.task

import oracle.jdbc.OracleTypes
import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.crypto.CryptoConfig
import ru.barabo.observer.config.cbr.ptkpsd.task.Send364pSign
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.Verba
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime

object SaveAccount311p : SingleSelector {
    override val select: String = "select r.id, a.code from od.PTKB_361P_REGISTER r, od.account a " +
            "where trunc(r.SENDDATE) = TRUNC(SYSDATE) and r.NUMBER_FILE > 0 and a.doc = r.idaccount"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
            LocalTime.of(11, 0),
            LocalTime.of(16, 0), Duration.ofSeconds(1))

    override fun name(): String = "311-П 2.Сохранить и зашифровать"

    override fun config(): ConfigTask = CryptoConfig

    private const val SELECT_FILENAME = "select od.PTKB_FNS_EXPORT_XML.getFileName(?) from dual"

    private const val SELECT_DATAFILE = "select od.PTKB_FNS_EXPORT_XML.getDataFileXml(?) from dual"

    private const val HEADER = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n"

    override fun execute(elem: Elem): State {
        val fileName = AfinaQuery.selectValue(SELECT_FILENAME, arrayOf(elem.idElem)) as String

        val dataFile = HEADER + AfinaQuery.selectValue(SELECT_DATAFILE, arrayOf(elem.idElem))

        val file = fullFile(fileName)

        file.writeText(dataFile, Charset.forName("CP1251") )

        val cryptoFolder = Cmd.createFolder("${file.parent}/CRYPTO")

        val cryptoFile = File("${cryptoFolder.absolutePath}/${file.name}")

        file.copyTo(cryptoFile, true)

        Verba.signByBarabo(cryptoFile)

        Verba.cryptoFile(cryptoFile)

        val archive = archiveName(elem.idElem, cryptoFile)

        Archive.addToArj(archive.absolutePath, arrayOf(cryptoFile) )

        return State.OK
    }

    private fun String.isPhysic() = indexOf("SF") == 0

    private fun archiveName(idFile: Long?, fileToArchive: File): File {

        val archive = AfinaQuery.execute(query = EXEC_ADD_TO_ARCHIVE,
                params = arrayOf(fileToArchive.nameWithoutExtension, idFile),
                outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

        return File("${fileToArchive.parent}/$archive.ARJ")
    }

    private fun fullFile(fileName :String): File {

        val folder = if (fileName.isPhysic() ) physicFolder() else juricFoler()

        return File("${folder.absolutePath}/$fileName")
    }

    fun cryptoFolder(isPhysic: Boolean): String =  if(isPhysic) physicFolder().absolutePath else juricFoler().absolutePath + "/CRYPTO"

    private fun physicFolder(): File = Cmd.createFolder("X:/311-П/ФИЗИКИ/Отправка/${Send364pSign.todayFolder()}")

    private fun juricFoler(): File = Cmd.createFolder("X:/311-П/Отправка/${Send364pSign.todayFolder()}")

    private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.addToArchive311p(?, ?, ?) }"
}