package ru.barabo.observer.config.skad.crypto.task

import oracle.jdbc.OracleTypes
import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.skad.crypto.p311.MessageCreator311p
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.crypto.ScadComplex
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime

object CreateCrypto311p512 : SingleSelector {
    override val select: String = "select r.id, a.code from od.PTKB_361P_REGISTER r, od.account a " +
            "where r.state = 0 and trunc(r.SENDDATE) = TRUNC(SYSDATE) and r.NUMBER_FILE > 0 and a.doc = r.idaccount"

    override val accessibleData: AccessibleData = AccessibleData(
        WeekAccess.WORK_ONLY, false,
        LocalTime.of(9, 0),
        LocalTime.of(15, 55), Duration.ofSeconds(1))

    override fun name(): String = "311-П 5.12 Создать+шифровать"

    override fun config(): ConfigTask = ScadConfig

    override fun execute(elem: Elem): State {

        val file = MessageCreator311p.createMessage(elem.idElem!!)

        val cryptoFolder = Cmd.createFolder("${file.parent}/CRYPTO")

        val cryptoFile = File("${cryptoFolder.absolutePath}/${file.name}")

        ScadComplex.fullEncode311p(file, cryptoFile)

        val archive = archiveName(elem.idElem, cryptoFile)

        Archive.addToArj(archive.absolutePath, arrayOf(cryptoFile) )

        return State.OK
    }

    private fun archiveName(idFile: Long?, fileToArchive: File): File {

        val archive = AfinaQuery.execute(query = EXEC_ADD_TO_ARCHIVE,
            params = arrayOf(fileToArchive.nameWithoutExtension, idFile),
            outParamTypes = intArrayOf(OracleTypes.VARCHAR))?.get(0) as String

        return File("${fileToArchive.parent}/$archive.ARJ")
    }
}

private const val EXEC_ADD_TO_ARCHIVE = "{ call od.PTKB_440P.addToArchive311p(?, ?, ?) }"