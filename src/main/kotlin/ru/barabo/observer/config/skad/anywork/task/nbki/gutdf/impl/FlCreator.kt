package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import com.sun.jmx.snmp.Timestamp
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectEventDataFL
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_1
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_2
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_3
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.FlEvent1_4
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.*

internal fun createPhysicEvent(eventRecord: EventRecord, priorPhysic: SubjectFl?): SubjectFl {

    var newPhysic: SubjectFl = if(priorPhysic?.idClient == eventRecord.clientId) priorPhysic
    else createPhysicTitle(eventRecord.clientId)

    newPhysic.events.addNewPhysicEvent(eventRecord)

    return newPhysic
}

internal fun createPhysicTitle(clientId: Long): SubjectFl {

    val titleData = AfinaQuery.selectCursor(SEL_TITLE_PHYSIC, arrayOf<Any?>(clientId))

    val titlePhysic = TitlePhysic(titleData[0])

    val subjectTitle = subjectTitleFromTitlePhysic(titlePhysic)

    return SubjectFl(clientId, subjectTitle, SubjectEventDataFL())
}

private fun subjectTitleFromTitlePhysic(title: TitlePhysic): SubjectTitleDataFl {

    val fio = Fl1Name(title.lastName, title.firstName, title.middleName)

    val flPrevName = if(title.prevLastName == null)
        Fl2PrevName()
    else
        Fl2PrevName(title.prevLastName, title.prevFirstName, title.prevMiddleName)

    val fl3Birth = Fl3Birth(title.birthDate.date, title.birthPlace)

    val fl4Doc = Fl4Doc(title.docCode, title.docSeries, title.docNum, title.issueDate.date, title.docIssuer,
        title.deptCode, title.foreignerCode)

    val fl5PrevDoc = if(title.prevDocCode== null)
        Fl5PrevDoc()
    else
        Fl5PrevDoc(title.prevDocCode, title.prevDocSeries, title.prevDocNum, title.prevIssueDate?.date,
            title.prevDocIssuer, title.prevDeptCode)

    val fl6Tax = title.inn?.let { Fl6Tax(it, title.ogrn, title.isSpecialMode) }

    val fl7Social = title.snils?.let { Fl7Social(it) }

    return SubjectTitleDataFl(Fl1_4Group(fio, fl4Doc), Fl2_5Group(flPrevName, fl5PrevDoc), fl3Birth, fl6Tax, fl7Social)
}

private fun SubjectEventDataFL.addNewPhysicEvent(eventRecord: EventRecord) {

    when(eventRecord.event) {
        "1.1" -> this.addEvent1_1(createFlEvent1_1(eventRecord) )
        "1.2" -> this.addEvent1_2(createFlEvent1_2(eventRecord) )
        "1.3" -> this.addEvent1_3(createFlEvent1_3(eventRecord) )
        //"1.4" -> this.addEvent1_4(createFlEvent1_4(eventRecord) )
    }
}

private fun createFlEvent1_1(eventRecord: EventRecord): FlEvent1_1 {

    val application55 = createFl55Application(eventRecord.loan, eventRecord.dateEvent)

    return FlEvent1_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, application55)
}

private fun createFlEvent1_2(eventRecord: EventRecord): FlEvent1_2 {

    val application55 = createFl55Application(eventRecord.loan, eventRecord.dateEvent)

    val (operationCode, fl291) = createFl291(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    return FlEvent1_2(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, operationCode, application55, fl291)
}

private fun createFlEvent1_3(eventRecord: EventRecord): FlEvent1_3 {

    val application55 = createFl55Application(eventRecord.loan, eventRecord.dateEvent)

    val (_, fl291) = createFl291(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl57 = createFl57(eventRecord.loan)

    return FlEvent1_3(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, application55, fl291, fl57)
}

private fun createFlEvent1_4(eventRecord: EventRecord): FlEvent1_4? {
/*
    val fl8AddrReg = createFl8AddrReg(eventRecord.clientId)

    val fl9AddrFact = createFl9AddrFact(eventRecord.clientId)

    val fl10Contact = createFl10Contact(eventRecord.clientId)

    val fl11IndividualEntrepreneur = createFl11IndividualEntrepreneur(eventRecord.clientId)

    val fl12Capacity = Fl12Capacity()

    val fl17DealUid = Fl17DealUid()


 Fl18Deal fl18Deal, Fl19Amount fl19Amount,
    List<Fl19_1AmountInfo> fl19_1AmountInfoList, Fl21PaymentTerms fl21PaymentTerms,
    Fl20JointDebtors fl20JointDebtors, Fl22TotalCost fl22TotalCost,
    Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo, Fl54Accounting fl54Accounting,
    Fl55Application fl55Application, Fl56Participation fl56Participation

    val application55 = createFl55Application(eventRecord.loan, eventRecord.dateEvent)

    val (_, fl291) = createFl291(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl57 = createFl57(eventRecord.loan)

    return FlEvent1_4(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, application55, fl291, fl57)

 */
   return null
}

private fun createFl55Application(loan: Long, dateEvent: Timestamp): Fl55Application {

    val data = AfinaQuery.selectCursor(SEL_FL_55, arrayOf<Any?>(loan, dateEvent))

    val fl55 = Fl55(data[0])

    return Fl55Application(fl55.role, fl55.sum, fl55.uid, fl55.applicationDate.date, fl55.approvalEndDate?.date,
        fl55.stageEndDate?.date, fl55.purposeCode, fl55.stageCode, fl55.stageDate.date, fl55.num, fl55.loanSum)
}

private fun createFl291(loan: Long, dateEvent: Timestamp, client: Long): Pair<String, Fl29_1DebtBurdenInfo?> {

    val data = AfinaQuery.selectCursor(SEL_FL_29_1, arrayOf<Any?>(loan, dateEvent, client))

    val fl291 = Fl291(data[0])

    val fl = if(fl291.loadRange == null) null else
        Fl29_1DebtBurdenInfo(fl291.loadRange, fl291.loadCalcDate?.date, fl291.incomeInfo, fl291.incomeInfoSource,
            fl291.isLoadFact, fl291.isLoadCalculationFact, fl291.dealUID)

    return Pair(fl291.opeationCode, fl)
}

private fun createFl57(loan: Long): Fl57Reject {

    val data = AfinaQuery.selectCursor(SEL_FL_57, arrayOf<Any?>(loan))[0]

    return Fl57Reject((data[0] as Timestamp).date, (data[1] as Number).toInt())
}

private const val SEL_TITLE_PHYSIC = "{ ? = call od.PTKB_GUTDF.getTitlePhysic( ? ) }"

private const val SEL_FL_55 = "{ ? = call od.PTKB_GUTDF.getFl55Application( ?, ? ) }"

private const val SEL_FL_29_1 = "{ ? = call od.PTKB_GUTDF.getFl29_1DebtBurdenInfo( ?, ?, ? ) }"

private const val SEL_FL_57 = "{ ? = call od.PTKB_GUTDF.getFFl57Reject( ? ) }"

private data class Fl291(
    val opeationCode: String,
    val loadRange: Int?,
    val loadCalcDate: Timestamp?,
    val incomeInfo: Int?,
    val incomeInfoSource: Int?,
    val isLoadFact: Boolean,
    val isLoadCalculationFact: Boolean,
    val dealUID: String?
) {
    constructor(rec: Array<Any?>) :
            this(opeationCode = (rec[0] as String),
                loadRange = (rec[1] as? Number)?.toInt(),
                loadCalcDate = (rec[2] as? Timestamp),
                incomeInfo = (rec[3] as? Number)?.toInt(),
                incomeInfoSource = (rec[4] as? Number)?.toInt(),
                isLoadFact = (rec[5] as? Number)?.toInt() != 0,
                isLoadCalculationFact = (rec[6] as? Number)?.toInt() != 0,
                dealUID = (rec[7] as? String)
            )
}

private data class Fl55(
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

private data class TitlePhysic(
    val lastName: String,
    val firstName: String,
    val middleName: String?,
    val prevLastName: String?,
    val prevFirstName: String?,
    val prevMiddleName: String?,
    val birthDate: Timestamp,
    val birthPlace: String,
    val docCode: String,
    val docSeries: String?,
    val docNum: String,
    val issueDate: Timestamp,
    val docIssuer: String?,
    val deptCode: String?,
    val foreignerCode: String,
    val prevDocCode: String?,
    val prevDocSeries: String?,
    val prevDocNum: String?,
    val prevIssueDate: Timestamp?,
    val prevDocIssuer: String?,
    val prevDeptCode: String?,
    val inn: String?,
    val ogrn: String?,
    val isSpecialMode: Boolean,
    val snils: String?

) {
    constructor(rec: Array<Any?>) :
            this(lastName = rec[0] as String,
                firstName = rec[1] as String,
                middleName = rec[2] as? String,
                prevLastName = rec[3] as? String,
                prevFirstName = rec[4] as? String,
                prevMiddleName = rec[5] as? String,
                birthDate = rec[6] as Timestamp,
                birthPlace = rec[7] as String,
                docCode = rec[8] as String,
                docSeries = rec[9] as? String,
                docNum = rec[10] as String,
                issueDate = rec[11] as Timestamp,
                docIssuer = rec[12] as? String,
                deptCode = rec[13] as? String,
                foreignerCode = rec[14] as String,
                prevDocCode = rec[15] as? String,
                prevDocSeries = rec[16] as? String,
                prevDocNum = rec[17] as? String,
                prevIssueDate = rec[18] as? Timestamp,
                prevDocIssuer = rec[19] as? String,
                prevDeptCode = rec[20] as? String,
                inn = rec[21] as String,
                ogrn = rec[22] as? String,
                isSpecialMode = (rec[23] as Number).toInt() != 0,
                snils = rec[24] as? String
            )
}