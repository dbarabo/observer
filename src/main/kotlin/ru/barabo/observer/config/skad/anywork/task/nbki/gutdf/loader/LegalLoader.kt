package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectTitleDataUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*
import ru.barabo.observer.config.task.nbki.gutdf.legal.event.*

private val logger = LoggerFactory.getLogger(GutdfLoaderFile::class.java)

fun processUl(idFile: Number, subjectUlList: List<SubjectUl>?): List<DataInfo> {

    if(subjectUlList.isNullOrEmpty()) return emptyList()

    val data = ArrayList<DataInfo>()

    for(ul in subjectUlList) {

        val tax = ul.title.ul4Tax.taxNumGroupUl4TaxList[0].taxNum.value!!

        var idMain: Number = 0

        ul.events?.ulEvent1_1List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)
            data.addAll(ul45Application(idMain, it.ul45Application))
        }

        ul.events.ulEvent1_2List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)

            data.addAll(ul45Application(idMain, it.ul45Application))
        }

        ul.events.ulEvent1_3List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)

            data.addAll(ul45Application(idMain, it.ul45Application))
        }

        ul.events.ulEvent1_4List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent1_7List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)
        }

        ul.events.ulEvent2_1List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent2_2List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent2_2_1List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)

            data.addAll( ul10DealUid(idMain, it.ul10DealUid) )
            data.addAll( ul11Deal(idMain, it.ul11Deal) )
            data.addAll( ul12Amount(idMain, it.ul12Amount) )
            data.addAll( ul121AmountInfo(idMain, it.ul12_1AmountInfoList) )
            data.addAll( ul14PaymentTerms(idMain, it.ul14PaymentTerms) )
            data.addAll(ul16Fund(idMain, it.ul16Fund))
            data.addAll( ul44Accounting(idMain, it.ul44Accounting) )
            data.addAll( ul46Participation(idMain, it.ul46Participation) )
        }

        ul.events.ulEvent2_3List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent2_4List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent2_5List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        ul.events.ulEvent2_6List?.forEach {
            idMain = findMainId(idFile, tax, it.event, it.unicalId, it.eventDate, it.orderNum)

            data.addAll( ul10DealUid(idMain, it.ul10DealUid) )
            data.addAll( ul30Court(idMain, it.ul30Court) )
        }

        ul.events.ulEvent3_2List?.forEach {
            val (id, dataList) = it.tags(idFile, tax)
            idMain = id
            data.addAll( dataList )
        }

        idMain.takeIf { it != 0 }?.let { data.addAll( title(it, ul.title) ) }
    }

    return data
}

private fun UlEvent1_4.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()

    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll( ul10DealUid(idMain, this.ul10DealUid) )
    data.addAll( ul11Deal(idMain, this.ul11Deal) )
    data.addAll( ul12Amount(idMain, this.ul12Amount) )
    data.addAll( ul121AmountInfo(idMain, this.ul12_1AmountInfoList) )
    data.addAll( ul14PaymentTerms(idMain, this.ul14PaymentTerms) )
    data.addAll( ul44Accounting(idMain, this.ul44Accounting) )
    data.addAll(ul45Application(idMain, this.ul45Application))
    data.addAll( ul46Participation(idMain, this.ul46Participation) )

    return Pair(idMain, data)
}
private fun UlEvent2_1.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()

    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll(ul10DealUid(idMain, this.ul10DealUid))
    data.addAll(ul11Deal(idMain, this.ul11Deal))
    data.addAll(ul12Amount(idMain, this.ul12Amount))
    data.addAll(ul121AmountInfo(idMain, this.ul12_1AmountInfoList))
    data.addAll(ul14PaymentTerms(idMain, this.ul14PaymentTerms))
    data.addAll(ul17181920Group(idMain, this.ul17_18_19_20Group))
    data.addAll(ul15ContractChanges(idMain, this.ul15ContractChanges))
    data.addAll(ul151ContractTermsChanges(idMain, this.ul15_1ContractTermsChanges))
    data.addAll(ul44Accounting(idMain, this.ul44Accounting))

    return Pair(idMain, data)
}

private fun UlEvent2_2.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()
    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll( ul10DealUid(idMain, this.ul10DealUid) )
    data.addAll( ul11Deal(idMain, this.ul11Deal) )
    data.addAll( ul12Amount(idMain, this.ul12Amount) )
    data.addAll( ul121AmountInfo(idMain, this.ul12_1AmountInfoList) )
    data.addAll( ul14PaymentTerms(idMain, this.ul14PaymentTerms) )
    data.addAll(ul16Fund(idMain, this.ul16Fund))
    data.addAll(ul17181920Group(idMain, this.ul17_18_19_20Group))
    data.addAll( ul44Accounting(idMain, this.ul44Accounting) )
    data.addAll( ul45Application(idMain, this.ul45Application) )
    data.addAll( ul46Participation(idMain, this.ul46Participation) )

    return Pair(idMain, data)
}

private fun UlEvent2_3.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()
    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll( ul10DealUid(idMain, this.ul10DealUid) )
    data.addAll( ul11Deal(idMain, this.ul11Deal) )
    data.addAll( ul12Amount(idMain, this.ul12Amount) )
    data.addAll( ul121AmountInfo(idMain, this.ul12_1AmountInfoList) )
    data.addAll( ul14PaymentTerms(idMain, this.ul14PaymentTerms) )
    data.addAll(ul17181920Group(idMain, this.ul17_18_19_20Group))
    data.addAll( ul44Accounting(idMain, this.ul44Accounting) )
    data.addAll( ul45Application(idMain, this.ul45Application) )
    data.addAll( ul46Participation(idMain, this.ul46Participation) )

    return Pair(idMain, data)
}

private fun UlEvent2_4.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()
    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll( ul10DealUid(idMain, this.ul10DealUid) )
    data.addAll( ul2326Group(idMain, this.ul23_26Group) )
    data.addAll( ul24Warranty(idMain, this.ul24Warranty) )

    return Pair(idMain, data)
}

private fun UlEvent2_5.tags(idFile: Number, tax: String, subEvent: String = ""): Pair<Number, List<DataInfo>> {

    val data = java.util.ArrayList<DataInfo>()
    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    data.addAll( ul10DealUid(idMain, this.ul10DealUid) )
    data.addAll( ul11Deal(idMain, this.ul11Deal) )
    data.addAll( ul12Amount(idMain, this.ul12Amount) )
    data.addAll( ul121AmountInfo(idMain, this.ul12_1AmountInfoList) )
    data.addAll( ul14PaymentTerms(idMain, this.ul14PaymentTerms) )
    data.addAll(ul17181920Group(idMain, this.ul17_18_19_20Group))

    data.addAll( ul29ContractEnd(idMain, this.ul29ContractEnd) )

    data.addAll( ul46Participation(idMain, this.ul46Participation) )

    return Pair(idMain, data)
}

private fun UlEvent3_2.subMainEvent(): String {
    return when {
        ulEvent14 != null -> "1.4"

        ulEvent21 != null -> "2.1"

        ulEvent22 != null -> "2.2"

        ulEvent23 != null -> "2.3"

        ulEvent24 != null -> "2.4"

        ulEvent25 != null -> "2.5"

        else -> ""
    }
}

private fun UlEvent3_2.tags(idFile: Number, tax: String): Pair<Number, List<DataInfo>> {

    val subEvent = subMainEvent()

    val idMain = findMainId(idFile, tax, this.event, this.unicalId, this.eventDate, this.orderNum, subEvent)

    val data = when {

        ulEvent14 != null -> {
            ulEvent14.event = "3.2"

            ulEvent14.tags(idFile, tax, subEvent).second
        }

        ulEvent21 != null -> {
            ulEvent21.event = "3.2"

            ulEvent21.tags(idFile, tax, subEvent).second
        }

        ulEvent22 != null -> {
            ulEvent22.event = "3.2"

            ulEvent22.tags(idFile, tax, subEvent).second
        }

        ulEvent23 != null -> {
            ulEvent23.event = "3.2"

            ulEvent23.tags(idFile, tax, subEvent).second
        }

        ulEvent24 != null -> {
            ulEvent24.event = "3.2"

            ulEvent24.tags(idFile, tax, subEvent).second
        }

        ulEvent25 != null -> {
            ulEvent25.event = "3.2"

            ulEvent25.tags(idFile, tax, subEvent).second
        }

        else -> emptyList()
    }

    return Pair(idMain, data)
}

private fun title(idMain: Number, title: SubjectTitleDataUl): List<DataInfo> {
    val data = java.util.ArrayList<DataInfo>()

    data += DataInfo(idMain, "UL_1_Name", "fullName", title.ul1Name.fullName?.value?:"")
    data += DataInfo(idMain, "UL_1_Name", "shortName", title.ul1Name.shortName?.value?:"")
    data += DataInfo(idMain, "UL_1_Name", "otherName", title.ul1Name.otherName?.value?:"")
    data += DataInfo(idMain, "UL_1_Name", "prevFull", title.ul1Name.prevFull?.value?:"")

    data += DataInfo(idMain, "UL_2_Address", "regStateNum", title.ul2Address.garAddr?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "locationCode", title.ul2Address.okato?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "street", title.ul2Address.street?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "house", title.ul2Address.house?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "estate", title.ul2Address.estate?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "block", title.ul2Address.block?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "build", title.ul2Address.build?.value?:"")
    data += DataInfo(idMain, "UL_2_Address", "apart", title.ul2Address.apart?.value?:"")

    data += DataInfo(idMain, "UL_2_Address", "phone",
        title.ul2Address.phoneList?.joinToString(";"){ it.phone.value } ?:"")
    data += DataInfo(idMain, "UL_2_Address", "email",
        title.ul2Address.emailList?.joinToString(";") { it.value } ?:"")

    data += DataInfo(idMain, "UL_3_Reg", "regNum", title.ul3Reg.ogrn?.value?:"")
    data += DataInfo(idMain, "UL_3_Reg", "lei", title.ul3Reg.lei?.value?:"")

    data += DataInfo(idMain, "UL_4_Tax", "taxNum", title.ul4Tax.taxNumGroupUl4TaxList[0].taxNum?.value?:"")

    return data
}

private fun ul30Court(idMain: Number, ul30: Ul30Court): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_30_Court", "ActExist", ul30.getActExist()?:""),
        DataInfo(idMain, "UL_30_Court", "date", ul30.date?.value?:""),

        DataInfo(idMain, "UL_30_Court", "num", ul30.num?.value?:""),
        DataInfo(idMain, "UL_30_Court", "actResolution", ul30.actResolutionCode?.value?:""),
        DataInfo(idMain, "UL_30_Court", "lawsuitCode", ul30.lawsuitCode?.value?:""),
        DataInfo(idMain, "UL_30_Court", "sumTotal", ul30.sumTotal?.value?:""),
        DataInfo(idMain, "UL_30_Court", "info", ul30.info?.value?:""),
        DataInfo(idMain, "UL_30_Court", "isActStarted", ul30.isActStarted.toDb())
    )
}

private fun ul29ContractEnd(idMain: Number, ul29: Ul29ContractEnd): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_29_ContractEnd", "date", ul29.date?.value?:""),
        DataInfo(idMain, "UL_29_ContractEnd", "num", ul29.code?.value?:"")
    )
}

private fun ul2326Group(idMain: Number, ul2326: Ul23_26Group): List<DataInfo> {
    if(ul2326.propertyIdGroupUl23_26GroupList.isNullOrEmpty() ) return emptyList()

    val data = java.util.ArrayList<DataInfo>()

    for(pledge in ul2326.propertyIdGroupUl23_26GroupList) {

        data.addAll(pledge.info(idMain))
    }

    return data
}

private fun PropertyIdGroupUl23_26Group.info(idMain: Number): List<DataInfo> {

    logger.error("idMain=$idMain")
    logger.error("propertyId=${propertyId?.value}")

    val id = AfinaQuery.selectValue(SEL_PLEDGE, params = arrayOf(idMain, propertyId?.value)) as Number

    val sum = if(ul23Collateral.sumGroupUl23_26Group.isNullOrEmpty()) null else ul23Collateral.sumGroupUl23_26Group[0]

    val list = mutableListOf(
        DataInfo(idMain, "UL_23_26_Group", "propertyId", propertyId?.value?:"", id),

        DataInfo(idMain, "UL_23_26_Group", "date", ul23Collateral.date?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "code", ul23Collateral.code?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "collateralEndDate", ul23Collateral.collateralEndDate?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "collateralFactEndDate", ul23Collateral.collateralFactEndDate?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "endReason", ul23Collateral.endReason?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "contractTotalSum", ul23Collateral.contractTotalSum?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "contractCount", ul23Collateral.contractCount?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "location", ul23Collateral.okato?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "actualCost", ul23Collateral.actualCost?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "calcDate", ul23Collateral.calcDate?.value?:"", id),

        DataInfo(idMain, "UL_23_26_Group", "sum", sum?.sum?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "assessDate", sum?.assessDate?.value?:"", id),
        DataInfo(idMain, "UL_23_26_Group", "priceCode", sum?.priceCode?.value?:"", id)
    )

    list.addAll(insureInfo(idMain, id) )

    return list
}

private fun PropertyIdGroupUl23_26Group.insureInfo(idMain: Number, idPledge: Number): List<DataInfo> {

    if(ul26Insurance.isNullOrEmpty()) return emptyList()

    val insureList = ul26Insurance.filter { !(it.startDate?.value.isNullOrEmpty()) }

    val data = ArrayList<DataInfo>()

    for((index, insure) in insureList.withIndex())  {
        data += DataInfo(idMain, "UL_26_INSURANCE_$index", "startDate", insure.startDate!!.value, idPledge)

        data += DataInfo(idMain, "UL_26_INSURANCE_$index", "insuranceEndDate", insure.insuranceEndDate?.value?:"", idPledge)

        data += DataInfo(idMain, "UL_26_INSURANCE_$index", "insuranceFactEndDate", insure.insuranceFactEndDate?.value?:"", idPledge)

        data += DataInfo(idMain, "UL_26_INSURANCE_$index", "endCode", insure.endCode?.value?:"", idPledge)
    }

    return data
}

private fun ul24Warranty(idMain: Number, ul24: Ul24Warranty): List<DataInfo> {
    if(ul24.uidGroupUl24WarrantyList.isNullOrEmpty() ) return emptyList()

    val data = ArrayList<DataInfo>()

    for(guarant in ul24.uidGroupUl24WarrantyList) {

        data.addAll(guarant.info(idMain))
    }

    return data
}

private fun UidGroupUl24Warranty.info(idMain: Number): List<DataInfo> {

    val id = AfinaQuery.selectValue(SEL_GUARANT_BY_ID, params = arrayOf(uid?.value)) as Number

    return listOf(
        DataInfo(idMain, "UL_24_Warranty", "uid", uid?.value?:"", id),
        DataInfo(idMain, "UL_24_Warranty", "sum", sum?.value?:"", id),
        DataInfo(idMain, "UL_24_Warranty", "openDate", openDate?.value?:"", id),
        DataInfo(idMain, "UL_24_Warranty", "endDate", endDate?.value?:"", id),
        DataInfo(idMain, "UL_24_Warranty", "factEndDate", factEndDate?.value?:"", id),
        DataInfo(idMain, "UL_24_Warranty", "endCode", endCode?.value?:"", id),
    )
}

private fun ul16Fund(idMain: Number, ul16: Ul16Fund): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_16_Fund", "date", ul16.date?.value?:""),
        DataInfo(idMain, "UL_16_Fund", "num", ul16.num?.value?:""),
        DataInfo(idMain, "UL_16_Fund", "startSum", ul16.startSum?.value?:"")
    )
}

private fun ul17181920Group(idMain: Number, ul: Ul17_18_19_20Group): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_17_18_19_20_Group", "isLastPay", ul.isLastPay?.toDb()?:"" ),
        DataInfo(idMain, "UL_17_18_19_20_Group", "calcDate", ul.calcDate?.value?:""),

        DataInfo(idMain, "UL_17_Debt", "debtSum", ul.ul17Debt.debtSum?.value?:""),
        DataInfo(idMain, "UL_17_Debt", "debtMainSum", ul.ul17Debt.debtMainSum?.value?:""),
        DataInfo(idMain, "UL_17_Debt", "debtPercentSum", ul.ul17Debt.debtPercentSum?.value?:""),
        DataInfo(idMain, "UL_17_Debt", "debtOtherSum", ul.ul17Debt.debtOtherSum?.value?:""),

        DataInfo(idMain, "UL_18_DebtDue", "debtDueSum", ul.ul18DebtDue.debtDueSum?.value?:""),
        DataInfo(idMain, "UL_18_DebtDue", "debtDueMainSum", ul.ul18DebtDue.debtDueMainSum?.value?:""),
        DataInfo(idMain, "UL_18_DebtDue", "debtDuePercentSum", ul.ul18DebtDue.debtDuePercentSum?.value?:""),
        DataInfo(idMain, "UL_18_DebtDue", "debtDueOtherSum", ul.ul18DebtDue.debtDueOtherSum?.value?:""),
        DataInfo(idMain, "UL_18_DebtDue", "debtDueStartDate", ul.ul18DebtDue.debtDueStartDate?.value?:""),

        DataInfo(idMain, "UL_19_DebtOverdue", "debtOverdueSum", ul.ul19DebtOverdue.debtOverdueSum?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "debtOverdueMainSum", ul.ul19DebtOverdue.debtOverdueMainSum?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "debtOverduePercentSum", ul.ul19DebtOverdue.debtOverduePercentSum?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "debtOverdueOtherSum", ul.ul19DebtOverdue.debtOverdueOtherSum?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "debtOverdueStartDate", ul.ul19DebtOverdue.debtOverdueStartDate?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "mainMissDate", ul.ul19DebtOverdue.mainMissDate?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "percentMissDate", ul.ul19DebtOverdue.percentMissDate?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "missDuration", ul.ul19DebtOverdue.missDuration?.value?:""),
        DataInfo(idMain, "UL_19_DebtOverdue", "repaidMissDuration", ul.ul19DebtOverdue.repaidMissDuration?.value?:""),

        DataInfo(idMain, "UL_20_Payment", "paymentSum", ul.ul20Payment.paymentSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "paymentMainSum", ul.ul20Payment.paymentMainSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "paymentPercentSum", ul.ul20Payment.paymentPercentSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "paymentOtherSum", ul.ul20Payment.paymentOtherSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "totalSum", ul.ul20Payment.totalSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "totalMainSum", ul.ul20Payment.totalMainSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "totalPercentSum", ul.ul20Payment.totalPercentSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "totalOtherSum", ul.ul20Payment.totalOtherSum?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "date", ul.ul20Payment.date?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "sizeCode", ul.ul20Payment.sizeCode?.value?:""),
        DataInfo(idMain, "UL_20_Payment", "scheduleCode", ul.ul20Payment.scheduleCode?.value?:"")
    )
}

private fun ul151ContractTermsChanges(idMain: Number, ul151: Ul15_1ContractTermsChanges): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_15_1_ContractTermsChanges", "termsChangeCode", ul151.termsChangeCode?.value?:""),
        DataInfo(idMain, "UL_15_1_ContractTermsChanges", "termsChangeDesc", ul151.termsChangeDesc?.value?:""),
        DataInfo(idMain, "UL_15_1_ContractTermsChanges", "changingDate", ul151.changingDate?.value?:"")
    )
}

private fun ul15ContractChanges(idMain: Number, ul15ContractChanges: Ul15ContractChanges): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_15_ContractChanges", "changeDate", ul15ContractChanges.changeDate?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "code", ul15ContractChanges.code?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "specialCode", ul15ContractChanges.specialCode?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "otherDesc", ul15ContractChanges.otherDesc?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "startDate", ul15ContractChanges.startDate?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "endDate", ul15ContractChanges.endDate?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "actualEndDate", ul15ContractChanges.actualEndDate?.value?:""),
        DataInfo(idMain, "UL_15_ContractChanges", "endCode", ul15ContractChanges.endCode?.value?:"")
    )
}

private fun ul46Participation(idMain: Number, ul46Participation: Ul46Participation): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_46_Participation", "role", ul46Participation.role?.value?:""),
        DataInfo(idMain, "UL_46_Participation", "kindCode", ul46Participation.kindCode?.value?:""),
        DataInfo(idMain, "UL_46_Participation", "uid", ul46Participation.uid?.value?:""),
        DataInfo(idMain, "UL_46_Participation", "fundDate", ul46Participation.fundDate?.value?:""),
        DataInfo(idMain, "UL_46_Participation", "isOverdue", ul46Participation.isOverdue.toDb()),
        DataInfo(idMain, "UL_46_Participation", "isStop", ul46Participation.isStop.toDb())
    )
}

private fun ul44Accounting(idMain: Number, ul44Accounting: Ul44Accounting): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_44_Accounting", "sum", ul44Accounting.sum?.value?:""),
        DataInfo(idMain, "UL_44_Accounting", "rate", ul44Accounting.rate?.value?:""),
        DataInfo(idMain, "UL_44_Accounting", "isSupport", ul44Accounting.isSupport.toDb()),
        DataInfo(idMain, "UL_44_Accounting", "supportInfo", ul44Accounting.supportInfo?.value?:""),
        DataInfo(idMain, "UL_44_Accounting", "calcDate", ul44Accounting.calcDate?.value?:"")
    )
}

private fun ul14PaymentTerms(idMain: Number, ul14PaymentTerms: Ul14PaymentTerms): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_14_PaymentTerms", "mainPaySum", ul14PaymentTerms.mainPaySum?.value?:""),
        DataInfo(idMain, "UL_14_PaymentTerms", "mainPayDate", ul14PaymentTerms.mainPayDate?.value?:""),
        DataInfo(idMain, "UL_14_PaymentTerms", "percentPaySum", ul14PaymentTerms.percentPaySum?.value?:""),
        DataInfo(idMain, "UL_14_PaymentTerms", "percentPayDate", ul14PaymentTerms.percentPayDate?.value?:""),
        DataInfo(idMain, "UL_14_PaymentTerms", "freqCode", ul14PaymentTerms.freqCode?.value?:""),
        DataInfo(idMain, "UL_14_PaymentTerms", "percentEndDate", ul14PaymentTerms.percentEndDate?.value?:"")
    )
}

private fun ul121AmountInfo(idMain: Number, ul121AmountInfoList: List<Ul12_1AmountInfo>?): List<DataInfo> {
    if(ul121AmountInfoList.isNullOrEmpty()) return emptyList()

    val ul121 = ul121AmountInfoList[0]

    return listOf(
        DataInfo(idMain, "UL_12_1_AmountInfo", "securitySum", ul121.securitySum?.value?:""),
        DataInfo(idMain, "UL_12_1_AmountInfo", "securityTypeCode", ul121.securityTypeCode?.value?:""),
        DataInfo(idMain, "UL_12_1_AmountInfo", "calcDate", ul121.calcDate?.value?:""),
        DataInfo(idMain, "UL_12_1_AmountInfo", "securityUid", ul121.securityUid?.value?:""),
        DataInfo(idMain, "UL_12_1_AmountInfo", "liabilityLimit", ul121.liabilityLimit?.value?:"")
    )
}

private fun ul12Amount(idMain: Number, ul12Amount: Ul12Amount): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_12_Amount", "sum", ul12Amount.sum?.value?:""),
        DataInfo(idMain, "UL_12_Amount", "calcDate", ul12Amount.calcDate?.value?:"")
    )
}

private fun ul11Deal(idMain: Number, ul11Deal: Ul11Deal): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_11_Deal", "role", ul11Deal.role?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "code", ul11Deal.code?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "kindCode", ul11Deal.kindCode?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "purposeCode", ul11Deal.purposeCode?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "endDate", ul11Deal.endDate?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "creditLineCode", ul11Deal.creditLineCode?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "startDate", ul11Deal.startDate?.value?:""),
        DataInfo(idMain, "UL_11_Deal", "isFloatRate", ul11Deal.isFloatRate.toDb())
    )
}

private fun ul10DealUid(idMain: Number, ul10DealUid: Ul10DealUid): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_10_DealUid", "uid", ul10DealUid.uid?.value?:""),
        DataInfo(idMain, "UL_10_DealUid", "num", ul10DealUid.num?.value?:""),
        DataInfo(idMain, "UL_10_DealUid", "refUid", ul10DealUid.refUid?.value?:""),
        DataInfo(idMain, "UL_10_DealUid", "openDate", ul10DealUid.openDate?.value?:"")
    )
}

private fun ul45Application(idMain: Number, ul45Application: Ul45Application): List<DataInfo> {
    return listOf(
        DataInfo(idMain, "UL_45_Application", "role", ul45Application.role?.value?:""),
        DataInfo(idMain, "UL_45_Application", "sum", ul45Application.sum?.value?:""),
        DataInfo(idMain, "UL_45_Application", "uid", ul45Application.uid?.value?:""),
        DataInfo(idMain, "UL_45_Application", "applicationDate", ul45Application.applicationDate?.value?:""),
        DataInfo(idMain, "UL_45_Application", "approvalEndDate", ul45Application.approvalEndDate?.value?:""),
        DataInfo(idMain, "UL_45_Application", "stageEndDate", ul45Application.stageEndDate?.value?:""),
        DataInfo(idMain, "UL_45_Application", "purposeCode", ul45Application.purposeCode?.value?:""),
        DataInfo(idMain, "UL_45_Application", "stageCode", ul45Application.stageCode?.value?:""),
        DataInfo(idMain, "UL_45_Application", "stageDate", ul45Application.stageDate?.value?:""),
        DataInfo(idMain, "UL_45_Application", "num", ul45Application.num?.value?:""),
        DataInfo(idMain, "UL_45_Application", "loanSum", ul45Application.loanSum?.value?:"")
    )
}

private fun findMainId(idFile: Number, tax: String, event: String,
                       unicalUid: String?, eventDateXml: String, orderNum: Int, subEvent: String = ""): Number {

    logger.error("tax=$tax")
    logger.error("event=$event")
    logger.error("unicalUid=$unicalUid")
    logger.error("eventDateXml=$eventDateXml")
    logger.error("subEvent=$subEvent")

    val id = when {
        (unicalUid == null) ->
            AfinaQuery.selectValue(SEL_MAIN_BY_TAX, params = arrayOf(idFile, tax, event,
                eventDateXml.xmlDateToTimestamp())) as Number

        event in listOf("1.1", "1.2", "1.3") ->
            AfinaQuery.selectValue(SEL_MAIN_BY_TAX, params = arrayOf(idFile, tax, event,
                eventDateXml.xmlDateToTimestamp())) as Number

        subEvent != "" -> AfinaQuery.selectValue(SEL_MAIN_BY_UID_SUBEVENT,
            params = arrayOf(idFile, unicalUid, event, eventDateXml.xmlDateToTimestamp(), subEvent) ) as Number

        else -> AfinaQuery.selectValue(SEL_MAIN_BY_UID,
            params = arrayOf(idFile, unicalUid, event, eventDateXml.xmlDateToTimestamp(), "") ) as Number

    }

    AfinaQuery.execute(UPDATE_MAIN_ORDER, params = arrayOf(orderNum, id))

    return id
}

private const val SEL_MAIN_BY_UID = "select od.PTKB_RUTDF.getMainByGuid(?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_UID_TAX = "select od.PTKB_RUTDF.getMainByGuidAndInnLegal(?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_TAX = "select od.PTKB_RUTDF.getMainByInnOrganization(?, ?, ?, ?) from dual"