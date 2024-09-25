package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import com.sun.jmx.snmp.Timestamp
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectEventDataFL
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl29_1DebtBurdenInfo
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl55Application
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl57Reject
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_1
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_2
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_3
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_4
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.*
import java.util.*

import java.util.Date

class GutdfDataFromRutdf(private val idRutdfFile: Long) : GutdfData {

    private val _data = createData(idRutdfFile)

    override fun filenameWithoutExt(): String {
        TODO("Not yet implemented")
    }

    override fun dateDocument(): Date {
        TODO("Not yet implemented")
    }

    override fun subjectsCount(): Int {
        TODO("Not yet implemented")
    }

    override fun groupBlocksCount(): Int {
        TODO("Not yet implemented")
    }

    override fun subjectFlList(): List<SubjectFl>? = _data.fl.ifEmpty { null }

    override fun subjectUlList(): List<SubjectUl>? = _data.ul.ifEmpty { null }

    private fun createData(idRutdfFile: Long): DataFlUl {

        val infoList = AfinaQuery.selectCursor(SEL_EVENT_BY_RUTDF, arrayOf<Any?>(idRutdfFile))

        val data = DataFlUl(ArrayList<SubjectFl>(), ArrayList<SubjectUl>())

        var priorPhysic: SubjectFl? = null

        var priorLegal: SubjectUl? = null

        for (record in infoList) {
            val eventRecord = EventRecord(record)

            if(eventRecord.isPhysic) {

                val newPhysic = createPhysicEvent(eventRecord, priorPhysic)

                if(newPhysic != priorPhysic) {

                    priorPhysic = newPhysic

                    data.fl += newPhysic
                }
            } else {

                val newLegal = createLegal(eventRecord, priorLegal)

                if(newLegal != priorLegal) {

                    priorLegal = newLegal

                    data.ul += newLegal
                }
            }
        }

        return data
    }
}

private const val SEL_EVENT_BY_RUTDF = "{ ? = call od.PTKB_GUTDF.getEventsByIdFile( ? ) }"

data class DataFlUl(val fl: MutableList<SubjectFl>, val ul: MutableList<SubjectUl>)

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