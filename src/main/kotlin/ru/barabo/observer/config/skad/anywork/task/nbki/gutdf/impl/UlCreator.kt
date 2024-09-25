package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import com.sun.jmx.snmp.Timestamp
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectEventDataUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectTitleDataUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul45Application
import ru.barabo.observer.config.task.nbki.gutdf.legal.event.UlEvent1_1
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.PhoneGroupFl10Contact
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement


internal fun createLegal(eventRecord: EventRecord, priorLegal: SubjectUl?): SubjectUl {

    val newLegal: SubjectUl = if(priorLegal?.idClient == eventRecord.clientId) priorLegal
                                    else createLegalTitle(eventRecord.clientId)

    newLegal.events.addNewEvent(eventRecord)

    return newLegal
}

private fun createLegalTitle(clientId: Long): SubjectUl {

    val titleData = AfinaQuery.selectCursor(SEL_TITLE_LEGAL, arrayOf<Any?>(clientId))

    val titleLegal = TitleLegal(titleData[0])

    val subjectTitle = subjectTitleFromTitleLegal(titleLegal)

    return SubjectUl(clientId, subjectTitle, SubjectEventDataUl())
}

private fun subjectTitleFromTitleLegal(title: TitleLegal): SubjectTitleDataUl {

    val name = Ul1Name(title.fullName, title.shortName, title.otherName, title.prevFull)

    val phoneList = title.phoneList?.split(";")?.map { PhoneGroupFl10Contact(it) }

    val emailList = title.emailList?.split(";")?.map { StringElement(it) }

    val ul2Address = Ul2Address(title.garAddr, title.okato, title.street, title.house, title.estate, title.build,
        title.block, title.apart, phoneList, emailList)

    val ogrn = Ul3RegType(title.ogrn, title.lei)

    val tax = TaxNumGroupUl4Tax(StringElement(title.taxCode), StringElement(title.taxNum) )

    val taxGroup = Ul4Tax(Ul4TaxType(listOf(tax)), null)

    return SubjectTitleDataUl(name, ul2Address, Ul3Reg(ogrn, null), taxGroup, null)
}

private fun SubjectEventDataUl.addNewEvent(eventRecord: EventRecord) {

    when(eventRecord.event) {
        "1.1" -> this.addEvent1_1(createEvent1_1(eventRecord) )
//        "1.2" -> this.addEvent1_2(createFlEvent1_2(eventRecord) )
//        "1.3" -> this.addEvent1_3(createFlEvent1_3(eventRecord) )
//        "1.4" -> this.addEvent1_4(createFlEvent1_4(eventRecord) )
//        "1.7" -> this.addEvent1_7(createFlEvent1_7(eventRecord) )
//        "1.9" -> this.addEvent1_9(createFlEvent1_9(eventRecord) )
//        "1.12" -> this.addEvent1_12(createFlEvent1_12(eventRecord) )
//        "2.1" -> this.addEvent2_1(createFlEvent2_1(eventRecord) )
//        "2.2" -> this.addEvent2_2(createFlEvent2_2(eventRecord) )
//        "2.2.1" -> this.addEvent2_2_1(createFlEvent2_2_1(eventRecord) )
//        "2.3" -> this.addEvent2_3(createFlEvent2_3(eventRecord) )
//        "2.4" -> this.addEvent2_4(createFlEvent2_4(eventRecord) )
//        "2.5" -> this.addEvent2_5(createFlEvent2_5(eventRecord) )
//        "2.6" -> this.addEvent2_6(createFlEvent2_6(eventRecord) )

        else -> throw Exception("event LEGAL not found event=${eventRecord.event}")
    }
}

private fun createEvent1_1(eventRecord: EventRecord): UlEvent1_1 {

    val ul45Application = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    return UlEvent1_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul45Application)
}

private fun createUl45Application(loan: Long, dateEvent: Timestamp): Ul45Application {

    val data = AfinaQuery.selectCursor(SEL_UL_45, arrayOf<Any?>(loan, dateEvent))

    val ul = Ul45(data[0])

    return Ul45Application(ul.role, ul.sum, ul.uid, ul.applicationDate.date, ul.approvalEndDate?.date,
        ul.stageEndDate?.date, ul.purposeCode, ul.stageCode, ul.stageDate.date, ul.num, ul.loanSum)
}

private data class Ul45(
    val role: String,
    val sum: Number?,
    val uid: String,
    val applicationDate: Timestamp,
    val approvalEndDate: Timestamp?,
    val stageEndDate: Timestamp?,
    val purposeCode: String?,
    val stageCode: String,
    val stageDate: Timestamp,
    val num: String?,
    val loanSum: Number?
) {
    constructor(rec: Array<Any?>) :
            this(role = (rec[0] as String),
                sum = (rec[1] as? Number),
                uid = (rec[2] as String),
                applicationDate = (rec[3] as Timestamp),
                approvalEndDate = (rec[4] as? Timestamp),
                stageEndDate = (rec[5] as? Timestamp),
                purposeCode = (rec[6] as? String),
                stageCode = (rec[7] as String),
                stageDate = (rec[8] as Timestamp),
                num = (rec[9] as? String),
                loanSum = (rec[10] as? Number)
            )
}

private data class TitleLegal(
    val fullName: String,
    val shortName: String,
    val otherName: String?,
    val prevFull: String?,

    val garAddr: String?,
    val okato: String?,
    val street: String?,
    val house: String?,
    val estate: String?,
    val build: String?,
    val block: String?,
    val apart: String?,
    val phoneList: String?,
    val emailList: String?,

    val ogrn: String,
    val lei: String?,

    val taxNum: String,
    val taxCode: String
) {
    constructor(rec: Array<Any?>) :
            this(fullName = rec[0] as String,
                shortName = rec[1] as String,
                otherName = rec[2] as? String,
                prevFull = rec[3] as? String,

                garAddr = rec[4] as? String,
                okato = rec[5] as? String,
                street = rec[6] as? String,
                house = rec[7] as? String,
                estate = rec[8] as? String,
                build = rec[9] as? String,
                block = rec[10] as? String,
                apart = rec[11] as? String,
                phoneList = rec[12] as? String,
                emailList = rec[13] as? String,

                ogrn = rec[14] as String,
                lei = rec[15] as? String,

                taxNum = rec[16] as String,
                taxCode = rec[17] as String
            )
}


private const val SEL_TITLE_LEGAL = "{ ? = call od.PTKB_GUTDF.getTitleLegal( ? ) }"

private const val SEL_UL_45 = "{ ? = call od.PTKB_GUTDF.getUl45Application( ?, ? ) }"
