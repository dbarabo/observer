package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import com.sun.jmx.snmp.Timestamp
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectEventDataUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectTitleDataUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*
import ru.barabo.observer.config.task.nbki.gutdf.legal.event.*
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement
import java.util.ArrayList

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
        "1.2" -> this.addEvent1_2(createEvent1_2(eventRecord) )
        "1.3" -> this.addEvent1_3(createEvent1_3(eventRecord) )
        "1.4" -> this.addEvent1_4(createEvent1_4(eventRecord) )
        "1.7" -> this.addEvent1_7(createEvent1_7(eventRecord) )
        "2.1" -> this.addEvent2_1(createEvent2_1(eventRecord) )
        "2.2" -> this.addEvent2_2(createlEvent2_2(eventRecord) )
        "2.2.1" -> this.addEvent2_2_1(createEvent2_2_1(eventRecord) )
        "2.3" -> this.addEvent2_3(createEvent2_3(eventRecord) )
        "2.4" -> this.addEvent2_4(createEvent2_4(eventRecord) )
        "2.5" -> this.addEvent2_5(createEvent2_5(eventRecord) )
        "2.6" -> this.addEvent2_6(createEvent2_6(eventRecord) )

        else -> throw Exception("event LEGAL not found event=${eventRecord.event}")
    }
}

private fun createEvent1_1(eventRecord: EventRecord): UlEvent1_1 {

    val (ul45Application, _) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    return UlEvent1_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul45Application)
}

private fun createEvent1_2(eventRecord: EventRecord): UlEvent1_2 {

    val (ul45Application, operationCode) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    return UlEvent1_2(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, operationCode, ul45Application)
}

private fun createEvent1_3(eventRecord: EventRecord): UlEvent1_3 {

    val (ul45Application, _) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    val ul47Reject = createUl47Reject(eventRecord.loan)

    return UlEvent1_3(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul45Application, ul47Reject)
}

private fun createEvent1_4(eventRecord: EventRecord): UlEvent1_4 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul13JointDebtors = createUl13JointDebtors(eventRecord.idEvent)

    val ul44Accounting = createUl44Accounting(eventRecord.idEvent)

    val (ul45Application, _) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    val ul46Participation = createUl46Participation(eventRecord.idEvent)

    return UlEvent1_4(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul13JointDebtors, ul44Accounting, ul45Application, ul46Participation)
}

private fun createEvent1_7(eventRecord: EventRecord): UlEvent1_7 {

    return UlEvent1_7(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date)
}

private fun createEvent2_1(eventRecord: EventRecord): UlEvent2_1 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul17181920Group = createUl17181920Group(eventRecord.idEvent)

    val ul13JointDebtors = createUl13JointDebtors(eventRecord.idEvent)

    val ul15ContractChanges = createUl15ContractChanges(eventRecord.idEvent)

    val ul151ContractTermsChanges = createUl151ContractTermsChanges(eventRecord.idEvent)

    val ul44Accounting = createUl44Accounting(eventRecord.idEvent)

    return UlEvent2_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul17181920Group, ul13JointDebtors, ul15ContractChanges,
        ul151ContractTermsChanges, ul44Accounting)
}

private fun createlEvent2_2(eventRecord: EventRecord): UlEvent2_2 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul17181920Group = createUl17181920Group(eventRecord.idEvent)

    val ul13JointDebtors = createUl13JointDebtors(eventRecord.idEvent)

    val ul16Fund = createUl16Fund(eventRecord.idEvent)

    val ul44Accounting = createUl44Accounting(eventRecord.idEvent)

    val (ul45Application, _) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    val ul46Participation = createUl46Participation(eventRecord.idEvent)

    return UlEvent2_2(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul17181920Group, ul13JointDebtors, ul16Fund,
        ul44Accounting, ul45Application, ul46Participation)
}

private fun createEvent2_2_1(eventRecord: EventRecord): UlEvent2_2_1 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul17181920Group = createUl17181920Group(eventRecord.idEvent)

    val ul16Fund = createUl16Fund(eventRecord.idEvent)

    val ul44Accounting = createUl44Accounting(eventRecord.idEvent)

    val ul46Participation = createUl46Participation(eventRecord.idEvent)

    return UlEvent2_2_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul17181920Group, ul16Fund,
        ul44Accounting, ul46Participation)
}

private fun createEvent2_3(eventRecord: EventRecord): UlEvent2_3 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul17181920Group = createUl17181920Group(eventRecord.idEvent)

    val ul13JointDebtors = createUl13JointDebtors(eventRecord.idEvent)

    val ul44Accounting = createUl44Accounting(eventRecord.idEvent)

    val (ul45Application, _) = createUl45Application(eventRecord.loan, eventRecord.dateEvent)

    val ul46Participation = createUl46Participation(eventRecord.idEvent)

    return UlEvent2_3(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul17181920Group, ul13JointDebtors,
        ul44Accounting, ul45Application, ul46Participation)
}

private fun createEvent2_4(eventRecord: EventRecord): UlEvent2_4 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul2326Group = createUl23_26Group(eventRecord.idEvent)

    val ul24Warranty = createUl24Warranty(eventRecord.idEvent)

    val ul25Guarantee = Ul25Guarantee()

    val ul27ProvisionPayment = Ul27ProvisionPayment()

    return UlEvent2_4(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul2326Group,
        ul24Warranty, ul25Guarantee, ul27ProvisionPayment)
}

private fun createEvent2_5(eventRecord: EventRecord): UlEvent2_5 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul11Deal = createUl11Deal(eventRecord.idEvent)

    val ul12Amount = createUl12Amount(eventRecord.idEvent)

    val ul121AmountInfo = createUl12_1AmountInfo(eventRecord.idEvent)

    val ul14PaymentTerms = createUl14PaymentTerms(eventRecord.idEvent)

    val ul17181920Group = createUl17181920Group(eventRecord.idEvent)

    val ul29ContractEnd = createUl29ContractEnd(eventRecord.idEvent)

    val ul46Participation = createUl46Participation(eventRecord.idEvent)

    return UlEvent2_5(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul11Deal, ul12Amount,
        listOf(ul121AmountInfo), ul14PaymentTerms, ul17181920Group, ul29ContractEnd, ul46Participation)
}

private fun createEvent2_6(eventRecord: EventRecord): UlEvent2_6 {

    val ul10DealUid = createUl10DealUid(eventRecord.idEvent)

    val ul30Court = createUl30Court(eventRecord.idEvent)

    return UlEvent2_6(eventRecord.orderNum.toInt(), eventRecord.dateEvent.date, ul10DealUid, ul30Court)
}

private fun createUl30Court(idEvent: Long): Ul30Court {

    val data = AfinaQuery.selectCursor(SEL_UL_30, arrayOf<Any?>(idEvent))

    val ul = Ul30(data[0])

    return when(ul.isExistsDisputeOrAct) {
        0 -> Ul30Court( false)
        1 -> Ul30Court( true)
        else -> Ul30Court(ul.date?.date, ul.num, ul.actResolutionCode, ul.isActStarted,
            ul.lawsuitCode, ul.sumTotal, ul.info)
    }
}

private fun createUl29ContractEnd(idEvent: Long): Ul29ContractEnd {

    val data = AfinaQuery.selectCursor(SEL_UL_29, arrayOf<Any?>(idEvent))[0]

    return Ul29ContractEnd((data[0] as Timestamp).date, (data[0] as Number).toInt())
}

private fun createUl23_26Group(idEvent: Long): Ul23_26Group {

    val data = AfinaQuery.selectCursor(SEL_UL_23_26, arrayOf<Any?>(idEvent))

    if(data.isEmpty()) return Ul23_26Group()

    val listGroup = ArrayList<PropertyIdGroupUl23_26Group>()

    for(rec in data) {
        val ul = Ul2326Group(rec)

        val sumGroupUl2326Group = if(ul.sum == null) null
        else listOf(SumGroupUl23_26Group(ul.sum, ul.assessDate?.date, ul.priceCode))

        val ul23Collateral = Ul23Collateral(ul.code, ul.date.date, sumGroupUl2326Group,
            ul.collateralEndDate?.date, ul.collateralFactEndDate?.date, ul.endReason, ul.contractTotalSum,
            ul.contractCount, ul.okato, ul.actualCost, ul.calcDate?.date)

        val insureList = if(ul.isExistsInsure) insureByCollateral(ul.idCollateral, idEvent) else listOf(Ul26Insurance())

        listGroup += PropertyIdGroupUl23_26Group(ul.propertyId, ul23Collateral, insureList)
    }

    return Ul23_26Group(listGroup)
}

private fun insureByCollateral(idCollateral:Number, idEvent: Long): List<Ul26Insurance> {

    val data = AfinaQuery.selectCursor(SEL_INSURE_BY_COLLATERAL, arrayOf<Any?>(idCollateral, idEvent))

    if(data.isEmpty()) return listOf(Ul26Insurance())

    val listGroup = ArrayList<Ul26Insurance>()

    for(rec in data) {
        val fl = UlInsure(rec)

        listGroup += Ul26Insurance(fl.startDate?.date, fl.insuranceEndDate?.date, fl.insuranceFactEndDate?.date, fl.endCode)
    }

    return listGroup
}

private fun createUl24Warranty(idEvent: Long): Ul24Warranty {

    val data = AfinaQuery.selectCursor(SEL_UL_24, arrayOf<Any?>(idEvent))

    if(data.isEmpty()) return Ul24Warranty()

    val listGroup = ArrayList<UidGroupUl24Warranty>()

    for(rec in data) {
        val ul = Ul24(rec)

        listGroup +=
            UidGroupUl24Warranty(ul.uid, ul.sum, ul.openDate?.date, ul.endDate?.date, ul.factEndDate?.date, ul.endCode)
    }

    return Ul24Warranty(listGroup)
}

private fun createUl16Fund(idEvent: Long): Ul16Fund {

    val data = AfinaQuery.selectCursor(SEL_UL_16, arrayOf<Any?>(idEvent))

    val fl = Ul16(data[0])

    return Ul16Fund(fl.date?.date, fl.num, fl.startSum)
}

private fun createUl151ContractTermsChanges(idEvent: Long): Ul15_1ContractTermsChanges {

    val data = AfinaQuery.selectCursor(SEL_UL_15_1, arrayOf<Any?>(idEvent))

    val ul = Ul151(data[0])

    return if(ul.termsChangeCode == null) Ul15_1ContractTermsChanges()
    else Ul15_1ContractTermsChanges(ul.termsChangeCode, ul.termsChangeDesc, ul.changingDate?.date)
}

private fun createUl15ContractChanges(idEvent: Long): Ul15ContractChanges {

    val data = AfinaQuery.selectCursor(SEL_UL_15, arrayOf<Any?>(idEvent))

    val ul = Ul15(data[0])

    return if(ul.code == null) Ul15ContractChanges()
    else Ul15ContractChanges(ul.changeDate?.date, ul.code, ul.specialCode, ul.otherDesc,
        ul.startDate?.date, ul.endDate?.date, ul.actualEndDate?.date, ul.endCode)
}

private fun createUl17181920Group(idEvent: Long): Ul17_18_19_20Group {
    val data = AfinaQuery.selectCursor(SEL_UL_17_18_19_20, arrayOf<Any?>(idEvent))

    val ul = UlGroup2528(data[0])

    val ul17Debt = if(ul.debtSum == null) Ul17Debt()
    else Ul17Debt(ul.debtSum, ul.debtMainSum, ul.debtPercentSum, ul.debtOtherSum)

    val ul18DebtDue = if(ul.debtDueSum == null || Math.round(ul.debtDueSum.toDouble()*100) == 0L) Ul18DebtDue()
    else Ul18DebtDue(ul.debtDueSum, ul.debtDueMainSum, ul.debtDuePercentSum, ul.debtDueOtherSum,
        ul.debtDueStartDate?.date)

    val ul19DebtOverdue = if(ul.debtOverdueSum == null || Math.round(ul.debtOverdueSum.toDouble()*100) == 0L) Ul19DebtOverdue()
    else Ul19DebtOverdue(ul.debtOverdueSum, ul.debtOverdueMainSum, ul.debtOverduePercentSum,
        ul.debtOverdueOtherSum, ul.debtOverdueStartDate?.date, ul.mainMissDate?.date,
        ul.percentMissDate?.date, ul.missDuration, ul.repaidMissDuration)

    val ul20Payment = if(ul.paymentSum == null || Math.round(ul.paymentSum.toDouble()*100) == 0L)
        Ul20Payment(ul.sizeCode, ul.scheduleCode)
    else Ul20Payment(ul.paymentSum, ul.paymentMainSum, ul.paymentPercentSum, ul.paymentOtherSum,
        ul.totalSum, ul.totalMainSum, ul.totalPercentSum, ul.totalOtherSum,
        ul.date?.date, ul.sizeCode, ul.scheduleCode)

    return Ul17_18_19_20Group(ul.isLastPayExist, ul.calcDate.date, ul17Debt, ul18DebtDue, ul19DebtOverdue, ul20Payment)
}

private fun createUl46Participation(idEvent: Long): Ul46Participation {

    val data = AfinaQuery.selectCursor(SEL_UL_46, arrayOf<Any?>(idEvent))

    val fl = Ul46(data[0])

    return Ul46Participation(fl.role, fl.kindCode, fl.uid, fl.fundDate?.date, fl.isOverdue90, fl.isStop)
}

private fun createUl44Accounting(idEvent: Long): Ul44Accounting {

    val data = AfinaQuery.selectCursor(SEL_UL_44, arrayOf<Any?>(idEvent))

    val ul = Ul44(data[0])

    return Ul44Accounting(ul.offBalanceAmount, ul.rate, ul.supportInfo, ul.calcDate?.date)
}

private fun createUl13JointDebtors(idEvent: Long): Ul13JointDebtors {

    val data = AfinaQuery.selectCursor(SEL_UL_13, arrayOf<Any?>(idEvent))[0]

    return if(data[0] == null) Ul13JointDebtors()
    else Ul13JointDebtors((data[0] as Number).toInt())
}

private fun createUl14PaymentTerms(idEvent: Long): Ul14PaymentTerms {

    val data = AfinaQuery.selectCursor(SEL_UL_14, arrayOf<Any?>(idEvent))

    val ul = Ul14(data[0])

    return if(ul.mainPaySum == null) Ul14PaymentTerms()
    else Ul14PaymentTerms(ul.mainPaySum, ul.mainPayDate?.date, ul.percentPaySum,
        ul.percentPayDate?.date, ul.freqCode, ul.percentEndDate?.date)
}

private fun createUl12_1AmountInfo(idEvent: Long): Ul12_1AmountInfo {

    val data = AfinaQuery.selectCursor(SEL_UL_121, arrayOf<Any?>(idEvent))

    val ul = Ul12(data[0])

    return if(ul.securityTypeCode == null) Ul12_1AmountInfo()
    else Ul12_1AmountInfo(ul.securitySum, ul.securityTypeCode, ul.calcDate?.date, ul.securityUid, ul.liabilityLimit)
}

private fun createUl12Amount(idEvent: Long): Ul12Amount {

    val data = AfinaQuery.selectCursor(SEL_UL_12, arrayOf<Any?>(idEvent))[0]

    return Ul12Amount(data[0] as Number, (data[1] as Timestamp).date)
}

private fun createUl11Deal(idEvent: Long): Ul11Deal {

    val data = AfinaQuery.selectCursor(SEL_UL_11, arrayOf<Any?>(idEvent))

    val ul = Ul11(data[0])

    return Ul11Deal(ul.role, ul.code, ul.kindCode, ul.purposeCode, ul.endDate?.date,
        ul.creditLineCode, ul.startDate.date, ul.isFloatRate)
}

private fun createUl10DealUid(idEvent: Long): Ul10DealUid {

    val data = AfinaQuery.selectCursor(SEL_UL_10, arrayOf<Any?>(idEvent))

    val ul = Ul10(data[0])

    return Ul10DealUid(ul.uid, ul.num, ul.refUid, ul.openDate.date)
}

private fun createUl47Reject(loan: Long): Ul47Reject {

    val data = AfinaQuery.selectCursor(SEL_UL_47, arrayOf<Any?>(loan))[0]

    return Ul47Reject((data[0] as Timestamp).date, (data[1] as Number).toInt())
}

private fun createUl45Application(loan: Long, dateEvent: Timestamp): Pair<Ul45Application, String?> {

    val data = AfinaQuery.selectCursor(SEL_UL_45, arrayOf<Any?>(loan, dateEvent))

    val ul = Ul45(data[0])

    return Pair(Ul45Application(ul.role, ul.sum, ul.uid, ul.applicationDate.date, ul.approvalEndDate?.date,
        ul.stageEndDate?.date, ul.purposeCode, ul.stageCode, ul.stageDate.date, ul.num, ul.loanSum), ul.operationCode)
}

private data class Ul30(
    val isExistsDisputeOrAct: Int,
    val date: Timestamp?,
    val num: String?,
    val actResolutionCode: Int?,
    val isActStarted: Boolean?,
    val lawsuitCode: Int?,
    val sumTotal: Number?,
    val info: String?) {

    constructor(rec: Array<Any?>) :
            this(
                isExistsDisputeOrAct = (rec[0] as Number).toInt(),
                date = (rec[1] as? Timestamp),
                num = (rec[2] as? String),
                actResolutionCode = (rec[3] as? Number)?.toInt(),
                isActStarted = rec[4]?.let { (it as Number).toInt() != 0 },
                lawsuitCode = (rec[5] as? Number)?.toInt(),
                sumTotal = (rec[6] as? Number),
                info = (rec[7] as? String)
            )
}

private data class UlInsure(
    val startDate: Timestamp?,
    val insuranceEndDate: Timestamp?,
    val insuranceFactEndDate: Timestamp?,
    val endCode: Int?) {

    constructor(rec: Array<Any?>) :
            this(
                startDate = (rec[0] as? Timestamp),
                insuranceEndDate = (rec[1] as? Timestamp),
                insuranceFactEndDate = (rec[2] as? Timestamp),
                endCode = (rec[3] as? Number)?.toInt()
            )
}

private data class Ul2326Group(
    val idCollateral: Number,
    val propertyId: String,
    val code: String,
    val date: Timestamp,
    val collateralEndDate: Timestamp?,
    val collateralFactEndDate: Timestamp?,
    val endReason: Int?,
    val contractTotalSum: Number,
    val contractCount: Int,
    val okato: String?,
    val actualCost: Number?,
    val calcDate: Timestamp?,

    val sum: Number?,
    val assessDate: Timestamp?,
    val priceCode: Int?,

    val isExistsInsure: Boolean) {

    constructor(rec: Array<Any?>) :
            this(
                idCollateral = (rec[0] as Number),
                propertyId = (rec[1] as String),
                code = (rec[2] as String),
                date = (rec[3] as Timestamp),
                collateralEndDate = (rec[4] as? Timestamp),
                collateralFactEndDate = (rec[5] as? Timestamp),
                endReason = (rec[6] as? Number)?.toInt(),
                contractTotalSum = (rec[7] as Number),
                contractCount = (rec[8] as Number).toInt(),
                okato = (rec[9] as? String),
                actualCost = (rec[10] as? Number),
                calcDate = (rec[11] as? Timestamp),

                sum = (rec[12] as? Number),
                assessDate = (rec[13] as? Timestamp),
                priceCode = (rec[14] as? Number)?.toInt(),
                isExistsInsure = ( (rec[15] != null) && ((rec[16] as Number).toInt() != 0))
            )
}

private data class Ul24(
    val uid: String,
    val sum: Number?,
    val openDate: Timestamp?,
    val endDate: Timestamp?,
    val factEndDate: Timestamp?,
    val endCode: Int?) {

    constructor(rec: Array<Any?>) :
            this(
                uid = (rec[0] as String),
                sum = (rec[1] as? Number),
                openDate = (rec[2] as? Timestamp),
                endDate = (rec[3] as? Timestamp),
                factEndDate = (rec[4] as? Timestamp),
                endCode = (rec[5] as? Number)?.toInt(),
            )
}

private data class Ul16(
    val date: Timestamp?,
    val num: Int?,
    val startSum: Number?) {

    constructor(rec: Array<Any?>) :
            this(
                date = (rec[0] as? Timestamp),
                num = (rec[1] as? Number)?.toInt(),
                startSum = (rec[2] as? Number)
            )
}

private data class Ul151(
    val termsChangeCode: Int?,
    val termsChangeDesc: String?,
    val changingDate: Timestamp?) {

    constructor(rec: Array<Any?>) :
            this(
                termsChangeCode = (rec[0] as? Number)?.toInt(),
                termsChangeDesc = (rec[1] as? String),
                changingDate = (rec[2] as? Timestamp)
            )
}

private data class Ul15(
    val changeDate: Timestamp?,
    val code: Int?,
    val specialCode: Int?,
    val otherDesc: String?,
    val startDate: Timestamp?,
    val endDate: Timestamp?,
    val actualEndDate: Timestamp?,
    val endCode: Int?) {

    constructor(rec: Array<Any?>) :
            this(
                changeDate = (rec[0] as? Timestamp),
                code = (rec[1] as? Number)?.toInt(),
                specialCode = (rec[2] as? Number)?.toInt(),
                otherDesc = (rec[3] as? String),
                startDate = (rec[4] as? Timestamp),
                endDate = (rec[5] as? Timestamp),
                actualEndDate = (rec[6] as? Timestamp),
                endCode = (rec[7] as? Number)?.toInt()
            )
}

private data class UlGroup2528(
    val isLastPayExist: Boolean?,
    val calcDate: Timestamp,

    val debtSum: Number?,
    val debtMainSum: Number?,
    val debtPercentSum: Number?,
    val debtOtherSum: Number?,

    val debtDueSum: Number?,
    val debtDueMainSum: Number?,
    val debtDuePercentSum: Number?,
    val debtDueOtherSum: Number?,
    val debtDueStartDate: Timestamp?,

    val debtOverdueSum: Number?,
    val debtOverdueMainSum: Number?,
    val debtOverduePercentSum: Number?,
    val debtOverdueOtherSum: Number?,
    val debtOverdueStartDate: Timestamp?,
    val mainMissDate: Timestamp?,
    val percentMissDate: Timestamp?,
    val missDuration: Int?,
    val repaidMissDuration: Int?,

    val paymentSum: Number?,
    val paymentMainSum: Number?,
    val paymentPercentSum: Number?,
    val paymentOtherSum: Number?,
    val totalSum: Number?,
    val totalMainSum: Number?,
    val totalPercentSum: Number?,
    val totalOtherSum: Number?,
    val date: Timestamp?,
    val sizeCode: Int,
    val scheduleCode: Int) {

    constructor(rec: Array<Any?>) :
            this(
                isLastPayExist = (rec[0] as? Number)?.let { it.toInt() != 0 },
                calcDate = (rec[1] as Timestamp),
                debtSum = (rec[2] as? Number),
                debtMainSum = (rec[3] as? Number),
                debtPercentSum = (rec[4] as? Number),
                debtOtherSum = (rec[5] as? Number),

                debtDueSum = (rec[6] as? Number),
                debtDueMainSum = (rec[7] as? Number),
                debtDuePercentSum = (rec[8] as? Number),
                debtDueOtherSum = (rec[9] as? Number),
                debtDueStartDate = (rec[10] as? Timestamp),

                debtOverdueSum = (rec[11] as? Number),
                debtOverdueMainSum = (rec[12] as? Number),
                debtOverduePercentSum = (rec[13] as? Number),
                debtOverdueOtherSum = (rec[14] as? Number),
                debtOverdueStartDate = (rec[15] as? Timestamp),
                mainMissDate = (rec[16] as? Timestamp),
                percentMissDate = (rec[17] as? Timestamp),
                missDuration = (rec[18] as? Number)?.toInt(),
                repaidMissDuration = (rec[19] as? Number)?.toInt(),

                paymentSum = (rec[20] as? Number),
                paymentMainSum = (rec[21] as? Number),
                paymentPercentSum = (rec[22] as? Number),
                paymentOtherSum = (rec[23] as? Number),
                totalSum = (rec[24] as? Number),
                totalMainSum = (rec[25] as? Number),
                totalPercentSum = (rec[26] as? Number),
                totalOtherSum = (rec[27] as? Number),
                date = (rec[28] as? Timestamp),
                sizeCode = (rec[29] as Number).toInt(),
                scheduleCode = (rec[30] as Number).toInt()
            )
}

private data class Ul46(
    val role: Int,
    val kindCode: Int,
    val uid: String,
    val fundDate: Timestamp?,
    val isOverdue90: Boolean,
    val isStop: Boolean) {

    constructor(rec: Array<Any?>) :
            this(
                role = (rec[0] as Number).toInt(),
                kindCode = (rec[1] as Number).toInt(),
                uid = (rec[2] as String),
                fundDate = (rec[3] as? Timestamp),
                isOverdue90 = (rec[4] as Number) != 0,
                isStop = (rec[5] as Number) != 0,
            )
}

private data class Ul44(
    val offBalanceAmount: Number?,
    val rate: Number?,
    val supportInfo: String?,
    val calcDate: Timestamp?) {

    constructor(rec: Array<Any?>) :
            this(
                offBalanceAmount = (rec[0] as? Number),
                rate = (rec[1] as? Number),
                supportInfo = (rec[2] as? String),
                calcDate = (rec[3] as? Timestamp)
            )
}

private data class Ul14(
    val mainPaySum: Number?,
    val mainPayDate: Timestamp?,
    val percentPaySum: Number?,
    val percentPayDate: Timestamp?,
    val freqCode: Int?,
    val percentEndDate: Timestamp?) {

    constructor(rec: Array<Any?>) :
            this(
                mainPaySum = (rec[0] as? Number),
                mainPayDate = (rec[1] as? Timestamp),
                percentPaySum = (rec[2] as? Number),
                percentPayDate = (rec[3] as? Timestamp),
                freqCode = (rec[4] as? Number)?.toInt(),
                percentEndDate = (rec[5] as? Timestamp)
            )
}

private data class Ul12(
    val securitySum: Number?,
    val securityTypeCode: Int?,
    val calcDate: Timestamp?,
    val securityUid: String?,
    val liabilityLimit: Long?) {

    constructor(rec: Array<Any?>) :
            this(
                securitySum = (rec[0] as? Number),
                securityTypeCode = (rec[1] as? Number)?.toInt(),
                calcDate = (rec[2] as? Timestamp),
                securityUid = (rec[3] as? String),
                liabilityLimit = (rec[4] as? Number)?.toLong()
            )
}

private data class Ul11(
    val role: Int,
    val code: Int,
    val kindCode: String?,
    val purposeCode: String?,
    val endDate: Timestamp?,
    val creditLineCode: String?,
    val startDate: Timestamp,
    val isFloatRate: Boolean) {

    constructor(rec: Array<Any?>) :
            this(
                role = (rec[0] as Number).toInt(),
                code = (rec[1] as Number).toInt(),
                kindCode = (rec[2] as? String),
                purposeCode = (rec[3] as? String),
                endDate = (rec[5] as? Timestamp),
                creditLineCode = (rec[6] as? String),
                startDate = (rec[7] as Timestamp),
                isFloatRate = (rec[8] as Number).toInt() != 0
            )
}

private data class Ul10(
    val uid: String,
    val num: String?,
    val refUid: String?,
    val openDate: Timestamp
) {
    constructor(rec: Array<Any?>) :
            this(
                uid = (rec[0] as String),
                num = (rec[1] as? String),
                refUid = (rec[2] as? String),
                openDate = (rec[3] as Timestamp)
            )
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
    val loanSum: Number?,
    val operationCode: String?
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
                loanSum = (rec[10] as? Number),
                operationCode = (rec[11] as? String)
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

private const val SEL_UL_47 = "{ ? = call od.PTKB_GUTDF.getUl47Reject( ? ) }"

private const val SEL_UL_10 = "{ ? = call od.PTKB_GUTDF.getUl10DealUid( ? ) }"

private const val SEL_UL_11 = "{ ? = call od.PTKB_GUTDF.getUl11Deal( ? ) }"

private const val SEL_UL_12 = "{ ? = call od.PTKB_GUTDF.getUl12Amount( ? ) }"

private const val SEL_UL_121 = "{ ? = call od.PTKB_GUTDF.getUl12_1AmountInfo( ? ) }"

private const val SEL_UL_14 = "{ ? = call od.PTKB_GUTDF.getUl14PaymentTerms( ? ) }"

private const val SEL_UL_13 = "{ ? = call od.PTKB_GUTDF.getUl13JointDebtors( ? ) }"

private const val SEL_UL_44 = "{ ? = call od.PTKB_GUTDF.getUl44Accounting( ? ) }"

private const val SEL_UL_46 = "{ ? = call od.PTKB_GUTDF.getUl46Participation( ? ) }"

private const val SEL_UL_17_18_19_20 = "{ ? = call od.PTKB_GUTDF.getUl17_18_19_20Group( ? ) }"

private const val SEL_UL_15 = "{ ? = call od.PTKB_GUTDF.getUl15ContractChanges( ? ) }"

private const val SEL_UL_15_1 = "{ ? = call od.PTKB_GUTDF.getUl15_1ContractTermsChanges( ? ) }"

private const val SEL_UL_16 = "{ ? = call od.PTKB_GUTDF.getUl16Fund( ? ) }"

private const val SEL_UL_24 = "{ ? = call od.PTKB_GUTDF.getUl24Warranty( ? ) }"

private const val SEL_INSURE_BY_COLLATERAL = "{ ? = call od.PTKB_GUTDF.getUlInsuranceByCollateral( ?, ? ) }"

private const val SEL_UL_23_26 = "{ ? = call od.PTKB_GUTDF.getUl23_26Group( ? ) }"

private const val SEL_UL_29 = "{ ? = call od.PTKB_GUTDF.getUl29ContractEnd( ? ) }"

private const val SEL_UL_30 = "{ ? = call od.PTKB_GUTDF.getUl30Court( ? ) }"