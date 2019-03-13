package ru.barabo.observer.config.barabo.plastic.release.add

import oracle.jdbc.OracleTypes
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.barabo.plastic.release.task.OutSmsData
import ru.barabo.observer.config.barabo.plastic.release.task.SendToJzdo
import ru.barabo.observer.config.barabo.plastic.release.task.iiaFile
import ru.barabo.observer.config.barabo.plastic.turn.task.OutIbi
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob

object OutApplicationData {

    private const val CREATE_FILE_APPLICATION = "{ call od.PTKB_PLASTIC_AUTO.createAppFileData(?, ?, ?) }"

    fun execute(idPacket: Long) {

        val iiaFile = iiaFile()

        val data = AfinaQuery.execute(query = CREATE_FILE_APPLICATION, params = arrayOf(idPacket, iiaFile),
                outParamTypes = intArrayOf(OracleTypes.CLOB))

        val text = data?.get(0)?.let { (it as Clob).clob2string() } ?: return

        val file = File("${OutIbi.hCardOutToday()}/$iiaFile")

        file.writeText(text, Charset.forName("CP1251"))

        SendToJzdo.executeFile(file)

        return
    }
}