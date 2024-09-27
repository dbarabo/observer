package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import com.sun.jmx.snmp.Timestamp
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import java.util.*

class GutdfDataFromRutdf(idRutdfFile: Long) : GutdfData {

    private val _data = createData(idRutdfFile)

    override fun filenameWithoutExt(): String = _data.fileName

    override fun dateDocument(): Date = _data.dateFile

    override fun subjectsCount(): Int = _data.subjectsCount

    override fun groupBlocksCount(): Int = _data.groupBlocksCount

    override fun subjectFlList(): List<SubjectFl>? = _data.fl.ifEmpty { null }

    override fun subjectUlList(): List<SubjectUl>? = _data.ul.ifEmpty { null }

    private fun createData(idRutdfFile: Long): DataFlUl {

        val (filename, dateFile) = AfinaQuery.select(SEL_FILENAME_DATA, params = arrayOf(idRutdfFile) )[0]

        val infoList = AfinaQuery.selectCursor(SEL_EVENT_BY_RUTDF, params = arrayOf(idRutdfFile))

        val fl = ArrayList<SubjectFl>()

        val ul = ArrayList<SubjectUl>()

        var priorPhysic: SubjectFl? = null

        var priorLegal: SubjectUl? = null

        for (record in infoList) {
            val eventRecord = EventRecord(record)

            if(eventRecord.isPhysic) {

                val newPhysic = createPhysicEvent(eventRecord, priorPhysic)

                if(newPhysic != priorPhysic) {

                    priorPhysic = newPhysic

                    fl += newPhysic
                }
            } else {

                val newLegal = createLegal(eventRecord, priorLegal)

                if(newLegal != priorLegal) {

                    priorLegal = newLegal

                    ul += newLegal
                }
            }
        }

        return DataFlUl(fl, ul, infoList.size, fl.size + ul.size,
            filename as String, (dateFile as Timestamp).date)
    }
}

data class DataFlUl(val fl: MutableList<SubjectFl>, val ul: MutableList<SubjectUl>,
                    val groupBlocksCount: Int, val subjectsCount: Int, val fileName: String, val dateFile: Date)

data class EventRecord(
    val orderNum: Long,
    val idEvent: Long,
    val isPhysic: Boolean,
    val clientId: Long,
    val dateEvent: Timestamp,
    val event: String,
    val loan: Long,
    val typeDeal: Int,
    val guarant: Long?,
    val guidId: Long?,
    val typeOverdue: Int,
    val account25Out: Long?,
    val flowOut: Timestamp?) {

    constructor(rec: Array<Any?>) :
            this(orderNum = (rec[0] as Number).toLong(),
                idEvent = (rec[1] as Number).toLong(),
                isPhysic = (rec[2] as Number).toInt() != 0,
                clientId = (rec[3] as Number).toLong(),
                dateEvent = (rec[4] as Timestamp),
                event = (rec[5] as String),
                loan = (rec[6] as Number).toLong(),
                typeDeal = (rec[7] as Number).toInt(),
                guarant = (rec[8] as? Number)?.toLong(),
                guidId = (rec[9] as? Number)?.toLong(),
                typeOverdue = (rec[10] as Number).toInt(),
                account25Out =  (rec[11] as? Number)?.toLong(),
                flowOut = (rec[12] as? Timestamp) )

}

private const val SEL_EVENT_BY_RUTDF = "{ ? = call od.PTKB_GUTDF.getEventsByIdFile( ? ) }"

private const val SEL_FILENAME_DATA = "select FILE_NAME, DATE_FILE from od.PTKB_RUTDF_FILE where ID = ?"