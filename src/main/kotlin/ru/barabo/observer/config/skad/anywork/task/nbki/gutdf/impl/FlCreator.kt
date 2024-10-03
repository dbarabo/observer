package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.impl

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectEventDataFL
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.*
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement
import java.sql.Timestamp
import java.util.*

internal fun createPhysicEvent(eventRecord: EventRecord, priorPhysic: SubjectFl?): SubjectFl {

    val newPhysic: SubjectFl = if(priorPhysic?.idClient == eventRecord.clientId) priorPhysic
                                else createPhysicTitle(eventRecord.clientId)

    newPhysic.events.addNewPhysicEvent(eventRecord)

    return newPhysic
}

private fun createPhysicTitle(clientId: Long): SubjectFl {

    val titleData = AfinaQuery.selectCursor(SEL_TITLE_PHYSIC, params = arrayOf(clientId))

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

    val fl3Birth = Fl3Birth(title.birthDate, title.birthPlace)

    val fl4Doc = Fl4Doc(title.docCode, title.docSeries, title.docNum, title.issueDate, title.docIssuer,
        title.deptCode, title.foreignerCode)

    val fl5PrevDoc = if(title.prevDocCode== null)
        Fl5PrevDoc()
    else
        Fl5PrevDoc(title.prevDocCode, title.prevDocSeries, title.prevDocNum, title.prevIssueDate,
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
        "1.4" -> this.addEvent1_4(createFlEvent1_4(eventRecord) )
        "1.7" -> this.addEvent1_7(createFlEvent1_7(eventRecord) )
        "1.9" -> this.addEvent1_9(createFlEvent1_9(eventRecord) )
        "1.12" -> this.addEvent1_12(createFlEvent1_12(eventRecord) )
        "2.1" -> this.addEvent2_1(createFlEvent2_1(eventRecord) )
        "2.2" -> this.addEvent2_2(createFlEvent2_2(eventRecord) )
        "2.2.1" -> this.addEvent2_2_1(createFlEvent2_2_1(eventRecord) )
        "2.3" -> this.addEvent2_3(createFlEvent2_3(eventRecord) )
        "2.4" -> this.addEvent2_4(createFlEvent2_4(eventRecord) )
        "2.5" -> this.addEvent2_5(createFlEvent2_5(eventRecord) )
        "2.6" -> this.addEvent2_6(createFlEvent2_6(eventRecord) )

        else -> throw Exception("event not found event=${eventRecord.event}")
    }
}

private fun createFlEvent1_1(eventRecord: EventRecord): FlEvent1_1 {

    val (_, application55) = createFl55Application(eventRecord.idEvent)

    return FlEvent1_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent, application55)
}

private fun createFlEvent1_2(eventRecord: EventRecord): FlEvent1_2 {

    val (operationCode, application55)  = createFl55Application(eventRecord.idEvent)

    val fl291 = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    return FlEvent1_2(eventRecord.orderNum.toInt(), eventRecord.dateEvent, operationCode, application55, fl291)
}

private fun createFlEvent1_3(eventRecord: EventRecord): FlEvent1_3 {

    val (_, application55) = createFl55Application(eventRecord.idEvent)

    val fl291 = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl57 = createFl57(eventRecord.loan)

    return FlEvent1_3(eventRecord.orderNum.toInt(), eventRecord.dateEvent, application55, fl291, fl57)
}

private fun createFlEvent1_4(eventRecord: EventRecord): FlEvent1_4 {

    val fl8AddrReg = createFl8AddrReg(eventRecord.clientId)

    val fl9AddrFact = createFl9AddrFact(eventRecord.clientId)

    val fl10Contact = createFl10Contact(eventRecord.clientId)

    val fl11IndividualEntrepreneur = createFl11IndividualEntrepreneur(eventRecord.clientId)

    val fl12Capacity = Fl12Capacity()

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl18Deal = createFl18Deal(eventRecord.idEvent)

    val fl19Amount = createFl19Amount(eventRecord.idEvent)

    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)

    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)

    val fl20JointDebtors = createFl20JointDebtors(eventRecord.idEvent)

    val fl22TotalCost = createFl22TotalCost(eventRecord.idEvent)

    val fl29_1DebtBurdenInfo = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl54Accounting = createFl54Accounting(eventRecord.idEvent)

    val (_, fl55Application) = createFl55Application(eventRecord.idEvent)

    val fl56Participation = createFl56Participation(eventRecord.idEvent)

    return FlEvent1_4(eventRecord.orderNum.toInt(), eventRecord.dateEvent, fl8AddrReg, fl9AddrFact, fl10Contact,
        fl11IndividualEntrepreneur, fl12Capacity, fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo),
        fl21PaymentTerms, fl20JointDebtors, fl22TotalCost, fl29_1DebtBurdenInfo, fl54Accounting, fl55Application,
        fl56Participation
    )
}

private fun createFlEvent1_7(eventRecord: EventRecord): FlEvent1_7 {

    return FlEvent1_7(eventRecord.orderNum.toInt(), eventRecord.dateEvent)
}

private fun createFlEvent1_9(eventRecord: EventRecord): FlEvent1_9 {

    val fl8AddrReg = createFl8AddrReg(eventRecord.clientId)

    val fl9AddrFact = createFl9AddrFact(eventRecord.clientId)

    val fl10Contact = createFl10Contact(eventRecord.clientId)

    val fl11IndividualEntrepreneur = createFl11IndividualEntrepreneur(eventRecord.clientId)

    return FlEvent1_9(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl8AddrReg, fl9AddrFact, fl10Contact, fl11IndividualEntrepreneur)
}

private fun createFlEvent1_12(eventRecord: EventRecord): FlEvent1_12 {

    return FlEvent1_12(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        Fl13Bankruptcy(), Fl14BankruptcyEnd() )
}

private fun createFlEvent2_1(eventRecord: EventRecord): FlEvent2_1 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl18Deal = createFl18Deal(eventRecord.idEvent)

    val fl19Amount = createFl19Amount(eventRecord.idEvent)

    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)

    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)

    val fl20JointDebtors = createFl20JointDebtors(eventRecord.idEvent)

    val fl22TotalCost = createFl22TotalCost(eventRecord.idEvent)

    val fl29_1DebtBurdenInfo = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl25_26_27_28Group = createFl25_26_27_28Group(eventRecord.idEvent)

    val fl29MonthlyPayment = createFl29MonthlyPayment(eventRecord.idEvent)

    val fl23ContractChanges = createFl23ContractChanges(eventRecord.idEvent)

    val fl231ContractTermsChanges = createFl23_1ContractTermsChanges(eventRecord.idEvent)

    val fl54Accounting = createFl54Accounting(eventRecord.idEvent)

    return FlEvent2_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo),
        fl21PaymentTerms, fl22TotalCost, fl25_26_27_28Group, fl29MonthlyPayment, fl29_1DebtBurdenInfo,
        fl20JointDebtors, fl23ContractChanges, fl231ContractTermsChanges, fl54Accounting)
}

private fun createFlEvent2_2(eventRecord: EventRecord): FlEvent2_2 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl18Deal = createFl18Deal(eventRecord.idEvent)

    val fl19Amount = createFl19Amount(eventRecord.idEvent)

    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)

    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)

    val fl20JointDebtors = createFl20JointDebtors(eventRecord.idEvent)

    val fl22TotalCost = createFl22TotalCost(eventRecord.idEvent)

    val fl29_1DebtBurdenInfo = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl24Fund = createFl24Fund(eventRecord.idEvent)

    val fl25_26_27_28Group = createFl25_26_27_28Group(eventRecord.idEvent)

    val fl29MonthlyPayment = createFl29MonthlyPayment(eventRecord.idEvent)

    val fl54Accounting = createFl54Accounting(eventRecord.idEvent)

    val (_, fl55Application) = createFl55Application(eventRecord.idEvent)

    val fl56Participation = createFl56Participation(eventRecord.idEvent)

    return FlEvent2_2(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo),
        fl21PaymentTerms, fl22TotalCost, fl24Fund, fl25_26_27_28Group, fl29MonthlyPayment, fl29_1DebtBurdenInfo,
        fl20JointDebtors, fl54Accounting, fl55Application, fl56Participation)
}

private fun createFlEvent2_2_1(eventRecord: EventRecord): FlEvent2_2_1 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl18Deal = createFl18Deal(eventRecord.idEvent)

    val fl19Amount = createFl19Amount(eventRecord.idEvent)

    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)

    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)

    val fl24Fund = createFl24Fund(eventRecord.idEvent)

    val fl54Accounting = createFl54Accounting(eventRecord.idEvent)

    val fl56Participation = createFl56Participation(eventRecord.idEvent)

    return FlEvent2_2_1(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo),
        fl21PaymentTerms, fl24Fund, fl54Accounting, fl56Participation)
}

private val logger = LoggerFactory.getLogger(GutdfDataFromRutdf::class.java)

private fun createFlEvent2_3(eventRecord: EventRecord): FlEvent2_3 {

    var time = System.currentTimeMillis()
    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl17DealUid=$time")

    time = System.currentTimeMillis()
    val fl18Deal = createFl18Deal(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl18Deal=$time")

    time = System.currentTimeMillis()
    val fl19Amount = createFl19Amount(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl19Amount=$time")

    time = System.currentTimeMillis()
    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl19_1AmountInfo=$time")

    time = System.currentTimeMillis()
    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl21PaymentTerms=$time")

    val fl20JointDebtors = createFl20JointDebtors(eventRecord.idEvent)

    time = System.currentTimeMillis()
    val fl22TotalCost = createFl22TotalCost(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl22TotalCost=$time")

    time = System.currentTimeMillis()
    val fl29_1DebtBurdenInfo = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl291DebtBurdenInfo=$time")

    time = System.currentTimeMillis()
    val fl25_26_27_28Group = createFl25_26_27_28Group(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("fl25_26_27_28Group=$time")

    time = System.currentTimeMillis()
    val fl29MonthlyPayment = createFl29MonthlyPayment(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl29MonthlyPayment=$time")

    time = System.currentTimeMillis()
    val fl54Accounting = createFl54Accounting(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl54Accounting=$time")

    time = System.currentTimeMillis()
    val (_, fl55Application) = createFl55Application(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl55Application=$time")

    time = System.currentTimeMillis()
    val fl56Participation = createFl56Participation(eventRecord.idEvent)
    time = (System.currentTimeMillis() - time)/100
    logger.error("createFl56Participation=$time")

    return FlEvent2_3(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo),
        fl21PaymentTerms, fl22TotalCost, fl25_26_27_28Group, fl29MonthlyPayment, fl29_1DebtBurdenInfo,
        fl20JointDebtors, fl54Accounting, fl55Application, fl56Participation)
}

private fun createFlEvent2_4(eventRecord: EventRecord): FlEvent2_4 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl3235Group = createFl32_35Group(eventRecord.idEvent)

    val fl33Warranty = createFl33Warranty(eventRecord.idEvent)

    val fl34Guarantee = createFl34Guarantee()

    val fl36ProvisionPayment = createFl36ProvisionPayment()

    return FlEvent2_4(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl3235Group, fl33Warranty, fl34Guarantee, fl36ProvisionPayment)
}

private fun createFlEvent2_5(eventRecord: EventRecord): FlEvent2_5 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl18Deal = createFl18Deal(eventRecord.idEvent)

    val fl19Amount = createFl19Amount(eventRecord.idEvent)

    val fl19_1AmountInfo = createFl19_1AmountInfo(eventRecord.idEvent)

    val fl21PaymentTerms = createFl21PaymentTerms(eventRecord.idEvent)

    val fl291DebtBurdenInfo = createFl291DebtBurdenInfo(eventRecord.loan, eventRecord.dateEvent, eventRecord.clientId)

    val fl22TotalCost = createFl22TotalCost(eventRecord.idEvent)

    val fl25262728Group = createFl25_26_27_28Group(eventRecord.idEvent)

    val fl29MonthlyPayment = createFl29MonthlyPayment(eventRecord.idEvent)

    val fl38ContractEnd = createFl38ContractEnd(eventRecord.idEvent)

    val fl56Participation = createFl56Participation(eventRecord.idEvent)

    return FlEvent2_5(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl18Deal, fl19Amount, listOf(fl19_1AmountInfo), fl21PaymentTerms, fl22TotalCost,
        fl25262728Group, fl29MonthlyPayment, fl291DebtBurdenInfo, fl38ContractEnd, fl56Participation)
}

private fun createFlEvent2_6(eventRecord: EventRecord): FlEvent2_6 {

    val fl17DealUid = createFl17DealUid(eventRecord.idEvent)

    val fl39Court = createFl39Court(eventRecord.idEvent)

    return FlEvent2_6(eventRecord.orderNum.toInt(), eventRecord.dateEvent,
        fl17DealUid, fl39Court)
}

private fun createFl39Court(idEvent: Long): Fl39Court {

    val data = AfinaQuery.selectCursor(SEL_FL_39, params = arrayOf(idEvent))

    val fl = Fl39(data[0])

    return when(fl.isExistsDisputeOrAct) {
        0 -> Fl39Court( false)
        1 -> Fl39Court( true)
        else -> Fl39Court(fl.date,fl.num, fl.actResolutionCode, fl.isActStarted,
            fl.lawsuitCode, fl.sumTotal, fl.info)
    }
}

private fun createFl38ContractEnd(idEvent: Long): Fl38ContractEnd {

    val data = AfinaQuery.selectCursor(SEL_FL_38, params = arrayOf(idEvent))[0]

    return Fl38ContractEnd((data[0] as Timestamp), (data[1] as Number).toInt())
}

private fun createFl36ProvisionPayment(): Fl36ProvisionPayment {
    return Fl36ProvisionPayment()
}

private fun createFl34Guarantee(): Fl34Guarantee {
    return Fl34Guarantee()
}

private fun createFl33Warranty(idEvent: Long): Fl33Warranty {

    val data = AfinaQuery.selectCursor(SEL_FL_33, params = arrayOf(idEvent))

    if(data.isEmpty()) return Fl33Warranty()

    val listGroup = ArrayList<UidGroupFl33Warranty>()

    for(rec in data) {
        val fl = Fl33(rec)

        listGroup +=
            UidGroupFl33Warranty(fl.uid, fl.sum, fl.openDate, fl.endDate, fl.factEndDate, fl.endCode)
    }

    return Fl33Warranty(listGroup)
}

private fun createFl32_35Group(idEvent: Long): Fl32_35Group {

    val data = AfinaQuery.selectCursor(SEL_FL_32_35, params = arrayOf(idEvent))

    if(data.isEmpty()) return Fl32_35Group()

    val listGroup = ArrayList<PropertyIdGroupFl32_35Group>()

    for(rec in data) {
        val fl = Fl3235Group(rec)

        val sumGroupFl3235GroupList = if(fl.sum == null) null
                                        else listOf(SumGroupFl32_35Group(fl.sum, fl.assessDate, fl.priceCode))

        val fl32Collateral = Fl32Collateral(fl.code, fl.date, sumGroupFl3235GroupList,
            fl.collateralEndDate, fl.collateralFactEndDate, fl.endReason, fl.contractTotalSum,
            fl.contractCount, fl.okato, fl.actualCost, fl.calcDate
        )

        val insureList = if(fl.isExistsInsure) insureByCollateral(fl.idCollateral, idEvent) else listOf(Fl35Insurance())

        listGroup += PropertyIdGroupFl32_35Group(fl.propertyId, fl32Collateral, insureList)
    }

    return Fl32_35Group(listGroup)
}

private fun insureByCollateral(idCollateral: Number, idEvent: Long): List<Fl35Insurance> {

    val data = AfinaQuery.selectCursor(SEL_INSURE_BY_COLLATERAL, params = arrayOf(idCollateral, idEvent))

    if(data.isEmpty()) return listOf(Fl35Insurance())

    val listGroup = ArrayList<Fl35Insurance>()

    for(rec in data) {
        val fl = FlInsure(rec)

        listGroup += Fl35Insurance(fl.startDate, fl.insuranceEndDate, fl.insuranceFactEndDate, fl.endCode)
    }

    return listGroup
}

private fun createFl24Fund(idEvent: Long): Fl24Fund {

    val data = AfinaQuery.selectCursor(SEL_FL_24, params = arrayOf(idEvent))

    val fl = Fl24(data[0])

    return Fl24Fund(fl.date, fl.num, fl.startSum)
}

private fun createFl25_26_27_28Group(idEvent: Long): Fl25_26_27_28Group {

    val data = AfinaQuery.selectCursor(SEL_FL_25_26_27_28, params = arrayOf(idEvent))

    val fl = FlGroup2528(data[0])

    val fl25Debt = if(fl.debtSum == null) Fl25Debt()
                    else Fl25Debt(fl.debtSum, fl.debtMainSum, fl.debtPercentSum, fl.debtOtherSum)

    val fl26DebtDue = if(fl.debtDueSum == null || Math.round(fl.debtDueSum.toDouble()*100) == 0L) Fl26DebtDue()
                        else Fl26DebtDue(fl.debtDueSum, fl.debtDueMainSum, fl.debtDuePercentSum, fl.debtDueOtherSum,
                            fl.debtDueStartDate)

    val fl27DebtOverdue = if(fl.debtOverdueSum == null || Math.round(fl.debtOverdueSum.toDouble()*100) == 0L) Fl27DebtOverdue()
                            else Fl27DebtOverdue(fl.debtOverdueSum, fl.debtOverdueMainSum, fl.debtOverduePercentSum,
                                fl.debtOverdueOtherSum, fl.debtOverdueStartDate, fl.mainMissDate,
                                fl.percentMissDate, fl.missDuration, fl.repaidMissDuration)

    val fl28Payment = if(fl.paymentSum == null || Math.round(fl.paymentSum.toDouble()*100) == 0L)
                        Fl28Payment(fl.sizeCode, fl.scheduleCode)
                        else Fl28Payment(fl.paymentSum, fl.paymentMainSum, fl.paymentPercentSum, fl.paymentOtherSum,
                            fl.totalSum, fl.totalMainSum, fl.totalPercentSum, fl.totalOtherSum,
                            fl.date, fl.sizeCode, fl.scheduleCode, fl.lastMissPaySum,
                            fl.paySum24)

    return Fl25_26_27_28Group(fl.isLastPayExist, fl.calcDate, fl25Debt, fl26DebtDue, fl27DebtOverdue, fl28Payment)
}

private fun createFl23_1ContractTermsChanges(idEvent: Long): Fl23_1ContractTermsChanges {

    val data = AfinaQuery.selectCursor(SEL_FL_231, params = arrayOf(idEvent))

    val fl = Fl231(data[0])

    return if(fl.termsChangeCode == null) Fl23_1ContractTermsChanges()
    else Fl23_1ContractTermsChanges(fl.termsChangeCode, fl.termsChangeDesc, fl.changingDate)
}

private fun createFl23ContractChanges(idEvent: Long): Fl23ContractChanges {

    val data = AfinaQuery.selectCursor(SEL_FL_23, params = arrayOf(idEvent))

    val fl = Fl23(data[0])

    return if(fl.code == null) Fl23ContractChanges()
          else Fl23ContractChanges(fl.changeDate, fl.code, fl.specialCode, fl.otherDesc,
            fl.startDate, fl.endDate, fl.actualEndDate, fl.endCode)
}

private fun createFl29MonthlyPayment(idEvent: Long): Fl29MonthlyPayment? {

    val data = AfinaQuery.selectCursor(SEL_FL_29, params = arrayOf(idEvent))

    val fl = Fl29(data[0])

    return if(fl.sum == null) null
            else Fl29MonthlyPayment(fl.sum, fl.calcDate, fl.sumTotal)
}

private fun createFl56Participation(idEvent: Long): Fl56Participation {

    val data = AfinaQuery.selectCursor(SEL_FL_56, params = arrayOf(idEvent))

    val fl = Fl56(data[0])

    return Fl56Participation(fl.role, fl.kindCode, fl.uid, fl.fundDate, fl.isOverdue90, fl.isStop)
}

private fun createFl54Accounting(idEvent: Long): Fl54Accounting {

    val data = AfinaQuery.selectCursor(SEL_FL_54, params = arrayOf(idEvent))

    val fl = Fl54(data[0])

    return Fl54Accounting(fl.offBalanceAmount, fl.minInterest, fl.maxInterest, fl.supportInfo, fl.calcDate)
}

private fun createFl22TotalCost(idEvent: Long): Fl22TotalCost {

    val data = AfinaQuery.selectCursor(SEL_FL_22, params = arrayOf(idEvent))

    val fl = Fl22(data[0])

    return if(fl.calcDate == null) Fl22TotalCost()
    else Fl22TotalCost(fl.minPercentCost, fl.minCost, fl.calcDate, fl.maxPercentCost, fl.maxCost)
}

private fun createFl20JointDebtors(idEvent: Long): Fl20JointDebtors {

    val data = AfinaQuery.selectCursor(SEL_FL_20, params = arrayOf(idEvent))[0]

    return if(data[0] == null) Fl20JointDebtors()
    else Fl20JointDebtors((data[0] as Number).toInt())
}

private fun createFl21PaymentTerms(idEvent: Long): Fl21PaymentTerms {

    val data = AfinaQuery.selectCursor(SEL_FL_21, params = arrayOf(idEvent))

    val fl = Fl21(data[0])

    return if(fl.mainPaySum == null) Fl21PaymentTerms()
    else Fl21PaymentTerms(fl.mainPaySum, fl.mainPayDate, fl.percentPaySum,
        fl.percentPayDate, fl.freqCode, fl.percentEndDate)
}

private fun createFl19_1AmountInfo(idEvent: Long): Fl19_1AmountInfo {

    val data = AfinaQuery.selectCursor(SEL_FL_19_1, params = arrayOf(idEvent))

    val fl = Fl19(data[0])

    return if(fl.securityTypeCode == null) Fl19_1AmountInfo()
           else Fl19_1AmountInfo(fl.securitySum, fl.securityTypeCode, fl.calcDate, fl.securityUid, fl.liabilityLimit)
}

private fun createFl19Amount(idEvent: Long): Fl19Amount {

    val data = AfinaQuery.selectCursor(SEL_FL_19, params = arrayOf(idEvent))[0]

    return Fl19Amount(data[0] as Number, (data[1] as Timestamp))
}

private fun createFl18Deal(idEvent: Long): Fl18Deal {

    val data = AfinaQuery.selectCursor(SEL_FL_18, params = arrayOf(idEvent))

    val fl = Fl18(data[0])

    return Fl18Deal(fl.role, fl.code, fl.kindCode, fl.purposeCode, fl.isConsumer, fl.endDate,
        fl.creditLineCode, fl.startDate, fl.isRepaymentFact)
}

private fun createFl17DealUid(idEvent: Long): Fl17DealUid {

    val data = AfinaQuery.selectCursor(SEL_FL_17, params = arrayOf(idEvent))

    val fl = Fl17(data[0])

    return Fl17DealUid(fl.uid, fl.num, fl.refUid, fl.openDate)
}

private fun createFl11IndividualEntrepreneur(client: Long): Fl11IndividualEntrepreneur {

    val data = AfinaQuery.selectCursor(SEL_FL_11, params = arrayOf(client))[0]

    val ogrn = (data[0] as? String) ?: return Fl11IndividualEntrepreneur()

    return Fl11IndividualEntrepreneur(ogrn, (data[1] as Timestamp))
}

private fun createFl10Contact(client: Long): Fl10Contact {

    val data = AfinaQuery.selectValue(SEL_FL_10, params = arrayOf(client) ) as? String

    val info = data?.split(";") ?: return Fl10Contact()

    val phones = info.filter {it.isNotEmpty() && (!it.contains('@')) }
        .map { PhoneGroupFl10Contact(it) }

    val mails = info.filter {it.isNotEmpty() && it.contains('@') }
        .map { StringElement(it) }

    return Fl10Contact(phones, mails)
}

private fun createFl9AddrFact(client: Long): Fl9AddrFact {

    val data = AfinaQuery.selectCursor(SEL_FL_9, params = arrayOf(client))

    val fl = Fl8(data[0])

    return if(fl.code != 1) Fl9AddrFact() else
        Fl9AddrFact(fl.postCode, fl.regStateNum, fl.okato, fl.street, fl.house, fl.estate, fl.block, fl.build, fl.apart)
}

private fun createFl8AddrReg(client: Long): Fl8AddrReg {

    val data = AfinaQuery.selectCursor(SEL_FL_8, params = arrayOf(client))

    val fl8 = Fl8(data[0])

    return Fl8AddrReg(fl8.code, fl8.postCode, fl8.regStateNum, fl8.okato,
        fl8.street, fl8.house, fl8.estate, fl8.block, fl8.build, fl8.apart)
}

private fun createFl55Application(idEvent: Long): Pair<String?, Fl55Application> {

    val data = AfinaQuery.selectCursor(SEL_FL_55, params = arrayOf(idEvent))

    val fl55 = Fl55(data[0])

    return Pair(fl55.operationCode,
        Fl55Application(fl55.role, fl55.sum, fl55.uid, fl55.applicationDate, fl55.approvalEndDate,
        fl55.stageEndDate, fl55.purposeCode, fl55.stageCode, fl55.stageDate, fl55.num, fl55.loanSum))
}

private fun createFl291DebtBurdenInfo(loan: Long, dateEvent: Timestamp, client: Long): Fl29_1DebtBurdenInfo? {

    val data = AfinaQuery.selectCursor(SEL_FL_29_1, params = arrayOf(loan, dateEvent, client))

    val fl291 = Fl291(data[0])

    return if(fl291.loadRange == null) null else
        Fl29_1DebtBurdenInfo(fl291.loadRange, fl291.loadCalcDate, fl291.incomeInfo, fl291.incomeInfoSource,
            fl291.isLoadFact, fl291.isLoadCalculationFact, fl291.dealUID)
}

private fun createFl57(loan: Long): Fl57Reject {

    val data = AfinaQuery.selectCursor(SEL_FL_57, params = arrayOf(loan))[0]

    return Fl57Reject((data[0] as Timestamp), (data[1] as Number).toInt())
}

private data class Fl39(
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

private data class FlInsure(
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

private data class Fl3235Group(
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
                isExistsInsure = ( (rec[15] != null) && ((rec[15] as Number).toInt() != 0))
            )
}

private data class Fl33(
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

private data class Fl24(
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

private data class Fl231(
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

private data class FlGroup2528(
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
    val scheduleCode: Int,
    val lastMissPaySum: Number?,
    val paySum24: Number?) {

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
                scheduleCode = (rec[30] as Number).toInt(),
                lastMissPaySum = (rec[31] as? Number),
                paySum24 = (rec[32] as? Number)
            )
}

private data class Fl23(
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

private data class Fl29(
    val sum: Long?,
    val calcDate: Timestamp?,
    val sumTotal: Number?) {

    constructor(rec: Array<Any?>) :
            this(
                sum = (rec[0] as? Number)?.toLong(),
                calcDate = (rec[1] as? Timestamp),
                sumTotal = (rec[2] as? Number)
            )
}

private data class Fl56(
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

private data class Fl54(
    val offBalanceAmount: Number?,
    val minInterest: Number?,
    val maxInterest: Number?,
    val supportInfo: String?,
    val calcDate: Timestamp?) {

    constructor(rec: Array<Any?>) :
            this(
                offBalanceAmount = (rec[0] as? Number),
                minInterest = (rec[1] as? Number),
                maxInterest = (rec[2] as? Number),
                supportInfo = (rec[3] as? String),
                calcDate = (rec[4] as? Timestamp)
            )
}

private data class Fl22(
    val minPercentCost: Number?,
    val minCost: Number?,
    val calcDate: Timestamp?,
    val maxPercentCost: Number?,
    val maxCost: Number?) {

    constructor(rec: Array<Any?>) :
            this(
                minPercentCost = (rec[0] as? Number),
                minCost = (rec[1] as? Number),
                calcDate = (rec[2] as? Timestamp),
                maxPercentCost = (rec[3] as? Number),
                maxCost = (rec[4] as? Number)
            )
}

private data class Fl21(
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

private data class Fl19(
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

private data class Fl18(
    val role: Int,
    val code: Int,
    val kindCode: String?,
    val purposeCode: String?,
    val isConsumer: Boolean,
    val endDate: Timestamp?,
    val creditLineCode: String?,
    val startDate: Timestamp,
    val isRepaymentFact: Boolean) {

    constructor(rec: Array<Any?>) :
            this(
                role = (rec[0] as Number).toInt(),
                code = (rec[1] as Number).toInt(),
                kindCode = (rec[2] as? String),
                purposeCode = (rec[3] as? String),
                isConsumer = (rec[4] as Number).toInt() != 0,
                endDate = (rec[5] as? Timestamp),
                creditLineCode = (rec[6] as? String),
                startDate = (rec[7] as Timestamp),
                isRepaymentFact = (rec[8] as Number).toInt() != 0
            )
}

private data class Fl17(
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

private data class Fl8(
    val code: Int?,
    val postCode: String,
    val regStateNum: String?,
    val okato: String,
    val street: String?,
    val house: String?,
    val estate: String?,
    val block: String?,
    val build: String?,
    val apart: String?
) {
    constructor(rec: Array<Any?>) :
            this(code = (rec[0] as? Number)?.toInt(),
                postCode = (rec[1] as String),
                regStateNum = (rec[2] as? String),
                okato = (rec[3] as String),
                street = (rec[4] as? String),
                house = (rec[5] as? String),
                estate = (rec[6] as? String),
                block = (rec[7] as? String),
                build = (rec[8] as? String),
                apart = (rec[9] as? String)
                )
}

private data class Fl291(
    val loadRange: Int?,
    val loadCalcDate: Timestamp?,
    val incomeInfo: Int?,
    val incomeInfoSource: Int?,
    val isLoadFact: Boolean,
    val isLoadCalculationFact: Boolean,
    val dealUID: String?
) {
    constructor(rec: Array<Any?>) :
            this(
                loadRange = (rec[0] as? Number)?.toInt(),
                loadCalcDate = (rec[1] as? Timestamp),
                incomeInfo = (rec[2] as? Number)?.toInt(),
                incomeInfoSource = (rec[3] as? Number)?.toInt(),
                isLoadFact = (rec[4] as? Number)?.toInt() != 0,
                isLoadCalculationFact = (rec[5] as? Number)?.toInt() != 0,
                dealUID = (rec[6] as? String)
            )
}

private data class Fl55(
    val operationCode: String?,
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
            this(operationCode =  (rec[0] as? String),
                role = (rec[1] as String),
                sum = (rec[2] as? Number),
                uid = (rec[3] as String),
                applicationDate = (rec[4] as Timestamp),
                approvalEndDate = (rec[5] as? Timestamp),
                stageEndDate = (rec[6] as? Timestamp),
                purposeCode = (rec[7] as? String),
                stageCode = (rec[8] as String),
                stageDate = (rec[9] as Timestamp),
                num = (rec[10] as? String),
                loanSum = (rec[11] as? Number)
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
                inn = rec[21] as? String,
                ogrn = rec[22] as? String,
                isSpecialMode = (rec[23] as Number).toInt() != 0,
                snils = rec[24] as? String
            )
}

private const val SEL_TITLE_PHYSIC = "{ ? = call od.PTKB_GUTDF.getTitlePhysic( ? ) }"

private const val SEL_FL_55 = "{ ? = call od.PTKB_GUTDF.getFl55Application( ? ) }"

private const val SEL_FL_29_1 = "{ ? = call od.PTKB_GUTDF.getFl29_1DebtBurdenInfo( ?, ?, ? ) }"

private const val SEL_FL_57 = "{ ? = call od.PTKB_GUTDF.getFl57Reject( ? ) }"

private const val SEL_FL_8 = "{ ? = call od.PTKB_GUTDF.getFl8AddrReg( ? ) }"

private const val SEL_FL_9 = "{ ? = call od.PTKB_GUTDF.getFl9AddrFact( ? ) }"

private const val SEL_FL_10 = "select od.PTKB_GUTDF.getFl10Contact( ? ) from dual"

private const val SEL_FL_11 = "{ ? = call od.PTKB_GUTDF.getFl11IndividualEntrepreneur( ? ) }"

private const val SEL_FL_17 = "{ ? = call od.PTKB_GUTDF.getFl17DealUid( ? ) }"

private const val SEL_FL_18 = "{ ? = call od.PTKB_GUTDF.getFl18Deal( ? ) }"

private const val SEL_FL_19_1 = "{ ? = call od.PTKB_GUTDF.getFl19_1AmountInfo( ? ) }"

private const val SEL_FL_19 = "{ ? = call od.PTKB_GUTDF.getFl19Amount( ? ) }"

private const val SEL_FL_21 = "{ ? = call od.PTKB_GUTDF.getFl21PaymentTerms( ? ) }"

private const val SEL_FL_20 = "{ ? = call od.PTKB_GUTDF.getFl20JointDebtors( ? ) }"

private const val SEL_FL_22 = "{ ? = call od.PTKB_GUTDF.getFl22TotalCost( ? ) }"

private const val SEL_FL_54 = "{ ? = call od.PTKB_GUTDF.getFl54Accounting( ? ) }"

private const val SEL_FL_56 = "{ ? = call od.PTKB_GUTDF.getFl56Participation( ? ) }"

private const val SEL_FL_29 = "{ ? = call od.PTKB_GUTDF.getFl29MonthlyPayment( ? ) }"

private const val SEL_FL_23 = "{ ? = call od.PTKB_GUTDF.getFl23ContractChanges( ? ) }"

private const val SEL_FL_231 = "{ ? = call od.PTKB_GUTDF.getFl23_1ContractTermsChanges( ? ) }"

private const val SEL_FL_25_26_27_28 = "{ ? = call od.PTKB_GUTDF.getFl25_26_27_28Group( ? ) }"

private const val SEL_FL_24 = "{ ? = call od.PTKB_GUTDF.getFl24Fund( ? ) }"

private const val SEL_FL_33 = "{ ? = call od.PTKB_GUTDF.getFl33Warranty( ? ) }"

private const val SEL_INSURE_BY_COLLATERAL = "{ ? = call od.PTKB_GUTDF.getFlInsuranceByCollateral( ?, ? ) }"

private const val SEL_FL_32_35 = "{ ? = call od.PTKB_GUTDF.getFl32_35Group( ? ) }"

private const val SEL_FL_38 = "{ ? = call od.PTKB_GUTDF.getFl38ContractEnd( ? ) }"

private const val SEL_FL_39 = "{ ? = call od.PTKB_GUTDF.getFl39Court( ? ) }"