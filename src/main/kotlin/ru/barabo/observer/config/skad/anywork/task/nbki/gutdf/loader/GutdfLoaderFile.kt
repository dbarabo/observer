package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.cbr.ibank.task.toTimestamp
import ru.barabo.observer.config.skad.plastic.task.SELECT_FILES_BY_NAME
import ru.barabo.observer.config.task.nbki.gutdf.MainDocument
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.*
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.SubjectTitleDataFl
import java.io.File
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object GutdfLoaderFile {

    fun loadByFile(file: File) {

        val mainDocument = loadFromXml(file)

        val idFile = AfinaQuery.selectValue(SELECT_FILES_BY_NAME, arrayOf(file.nameWithoutExtension.uppercase())) as Number

        processByFiles(idFile, mainDocument)
    }

    private fun processByFiles(idFile: Number, mainDocument: MainDocument) {

        val data = ArrayList<DataInfo>()

        data += processFl(idFile, mainDocument.data.subjectFlList)

        data += processUl(idFile, mainDocument.data.subjectUlList)

        data.saveAll(idFile)
    }

    private fun processFl(idFile: Number, subjectFlList: List<SubjectFl>?): List<DataInfo> {
        if(subjectFlList.isNullOrEmpty()) return emptyList()

        val data = ArrayList<DataInfo>()

        for(fl in subjectFlList) {

            val taxPassport = fl.title.unical

            var idMain: Number = 0

            fl.events.flEvent1_1List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)
                data.addAll( fl55Application(idMain, it.fl55Application) )
            }

            fl.events.flEvent1_2List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)
                data.addAll( fl55Application(idMain, it.fl55Application) )
            }

            fl.events.flEvent1_3List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)
                data.addAll( fl55Application(idMain, it.fl55Application) )
                data.addAll( fl57Reject(idMain, it.fl57Reject) )
            }

            fl.events.flEvent1_4List?.forEach {
                val (id, dataList) = it.tags(idFile, taxPassport)
                idMain = id
                data.addAll( dataList )
            }

            fl.events.flEvent1_7List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)
            }

            fl.events.flEvent1_9List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)
                data.addAll( fl8AddrReg(idMain, it.fl8AddrReg) )
                data.addAll( fl9AddrFact(idMain, it.fl9AddrFact) )
                data.addAll( fl10Contact(idMain, it.fl10Contact) )
                data.addAll( fl11IndividualEntrepreneur(idMain, it.fl11IndividualEntrepreneur) )
            }

            fl.events.flEvent2_1List?.forEach {
                val (id, dataList) = it.tags(idFile, taxPassport)
                idMain = id
                data.addAll( dataList )
            }

            fl.events.flEvent2_2List?.forEach {
                val (id, dataList) = it.tags(idFile, taxPassport)
                idMain = id
                data.addAll( dataList )
            }

            fl.events.flEvent2_2_1List?.forEach { event ->
                idMain = findMainId(idFile, taxPassport, event.event, event.unicalId, event.eventDate, event.orderNum)

                data.addAll( fl17DealUid(idMain, event.fl17DealUid) )
                data.addAll( fl18Deal(idMain, event.fl18Deal) )
                data.addAll( fl19Amount(idMain, event.fl19Amount) )
                data.addAll( fl191AmountInfo(idMain, event.fl19_1AmountInfoList) )
                data.addAll( fl21PaymentTerms(idMain, event.fl21PaymentTerms) )
                data.addAll( fl24Fund(idMain, event.fl24Fund) )
                data.addAll( fl54Accounting(idMain, event.fl54Accounting) )
                data.addAll( fl56Participation(idMain, event.fl56Participation) )
            }

            fl.events.flEvent2_3List?.forEach {
                val (id, dataList) = it.tags(idFile, taxPassport)
                idMain = id
                data.addAll( dataList )
            }

            fl.events.flEvent2_4List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)

                data.addAll( fl17DealUid(idMain, it.fl17DealUid) )
                data.addAll( fl3235Group(idMain, it.fl32_35Group) )
                data.addAll( fl33Warranty(idMain, it.fl33Warranty) )
            }

            fl.events.flEvent2_5List?.forEach { event ->
                idMain = findMainId(idFile, taxPassport, event.event, event.unicalId, event.eventDate, event.orderNum)

                data.addAll( fl17DealUid(idMain, event.fl17DealUid) )
                data.addAll( fl18Deal(idMain, event.fl18Deal) )
                data.addAll( fl19Amount(idMain, event.fl19Amount) )
                data.addAll( fl191AmountInfo(idMain, event.fl19_1AmountInfoList) )
                data.addAll( fl21PaymentTerms(idMain, event.fl21PaymentTerms) )
                data.addAll( fl22TotalCost(idMain, event.fl22TotalCost) )
                data.addAll( fl25262728Group(idMain, event.fl25_26_27_28Group) )

                event.fl29MonthlyPayment?.let { data.addAll( fl29MonthlyPayment(idMain, it) ) }

                event.fl29_1DebtBurdenInfo?.let { data.addAll( fl291DebtBurdenInfo(idMain, it) ) }

                data.addAll( fl38ContractEnd(idMain, event.fl38ContractEnd) )
                data.addAll( fl56Participation(idMain, event.fl56Participation) )
            }

            fl.events.flEvent2_6List?.forEach {
                idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate, it.orderNum)

                data.addAll( fl17DealUid(idMain, it.fl17DealUid) )
                data.addAll( fl39Court(idMain, it.fl39Court) )
            }

            fl.events.flEvent3_2List?.forEach {

                val (id, dataList) = it.tags(idFile, taxPassport)
                idMain = id
                data.addAll( dataList )
            }

            idMain.takeIf { it != 0 }?.let { data.addAll( title(it, fl.title) ) }
        }

        return data
    }

    private fun FlEvent3_2.tags(idFile: Number, taxPassport: String): Pair<Number, List<DataInfo>> {

        val idMain = findMainId(idFile, taxPassport, this.event, this.unicalId, this.eventDate, this.orderNum)

        val data = when {
            flEvent14 != null -> {
                flEvent14.event = "3.2"

                flEvent14.tags(idFile, taxPassport).second
            }

            flEvent21 != null -> {
                flEvent21.event = "3.2"

                flEvent21.tags(idFile, taxPassport).second
            }

            flEvent22 != null -> {
                flEvent22.event = "3.2"

                flEvent22.tags(idFile, taxPassport).second
            }

            flEvent23 != null -> {
                flEvent23.event = "3.2"

                flEvent23.tags(idFile, taxPassport).second
            }

            else -> emptyList()
        }

        return Pair(idMain, data )
    }

    private fun FlEvent1_4.tags(idFile: Number, taxPassport: String): Pair<Number, List<DataInfo>> {

        val data = ArrayList<DataInfo>()

        val idMain = findMainId(idFile, taxPassport, this.event, this.unicalId, this.eventDate, this.orderNum)

        data.addAll( fl8AddrReg(idMain, this.fl8AddrReg) )
        data.addAll( fl9AddrFact(idMain, this.fl9AddrFact) )
        data.addAll( fl10Contact(idMain, this.fl10Contact) )
        data.addAll( fl11IndividualEntrepreneur(idMain, this.fl11IndividualEntrepreneur) )
        data.addAll( fl17DealUid(idMain, this.fl17DealUid) )
        data.addAll( fl18Deal(idMain, this.fl18Deal) )
        data.addAll( fl19Amount(idMain, this.fl19Amount) )
        data.addAll( fl191AmountInfo(idMain, this.fl19_1AmountInfoList) )
        data.addAll( fl21PaymentTerms(idMain, this.fl21PaymentTerms) )
        data.addAll( fl22TotalCost(idMain, this.fl22TotalCost) )

        this.fl29_1DebtBurdenInfo?.let { data.addAll( fl291DebtBurdenInfo(idMain, it) ) }

        data.addAll( fl54Accounting(idMain, this.fl54Accounting) )
        data.addAll( fl55Application(idMain, this.fl55Application) )
        data.addAll( fl56Participation(idMain, this.fl56Participation) )

        return Pair(idMain, data)
    }

    private fun FlEvent2_2.tags(idFile: Number, taxPassport: String): Pair<Number, List<DataInfo>> {

        val data = ArrayList<DataInfo>()

        val idMain = findMainId(idFile, taxPassport, this.event, this.unicalId, this.eventDate, this.orderNum)

        data.addAll(fl17DealUid(idMain, this.fl17DealUid))
        data.addAll(fl18Deal(idMain, this.fl18Deal))
        data.addAll(fl19Amount(idMain, this.fl19Amount))
        data.addAll(fl191AmountInfo(idMain, this.fl19_1AmountInfoList))
        data.addAll(fl21PaymentTerms(idMain, this.fl21PaymentTerms))
        data.addAll(fl22TotalCost(idMain, this.fl22TotalCost))
        data.addAll(fl24Fund(idMain, this.fl24Fund))
        data.addAll(fl25262728Group(idMain, this.fl25_26_27_28Group))

        this.fl29MonthlyPayment?.let { data.addAll(fl29MonthlyPayment(idMain, it)) }

        this.fl29_1DebtBurdenInfo?.let { data.addAll(fl291DebtBurdenInfo(idMain, it)) }

        data.addAll(fl54Accounting(idMain, this.fl54Accounting))
        data.addAll(fl55Application(idMain, this.fl55Application))
        data.addAll(fl56Participation(idMain, this.fl56Participation))

        return Pair(idMain, data)
    }

    private fun FlEvent2_1.tags(idFile: Number, taxPassport: String): Pair<Number, List<DataInfo>> {

        val data = ArrayList<DataInfo>()

        val idMain = findMainId(idFile, taxPassport, this.event, this.unicalId, this.eventDate, this.orderNum)

        data.addAll(fl17DealUid(idMain, this.fl17DealUid))
        data.addAll(fl18Deal(idMain, this.fl18Deal))
        data.addAll(fl19Amount(idMain, this.fl19Amount))
        data.addAll(fl191AmountInfo(idMain, this.fl19_1AmountInfoList))
        data.addAll(fl21PaymentTerms(idMain, this.fl21PaymentTerms))
        data.addAll(fl22TotalCost(idMain, this.fl22TotalCost))
        data.addAll(fl25262728Group(idMain, this.fl25_26_27_28Group))

        this.fl29MonthlyPayment?.let { data.addAll(fl29MonthlyPayment(idMain, it)) }

        this.fl29_1DebtBurdenInfo?.let { data.addAll(fl291DebtBurdenInfo(idMain, it)) }

        data.addAll(fl23ContractChanges(idMain, this.fl23ContractChanges))
        data.addAll(fl231ContractTermsChanges(idMain, this.fl23_1ContractTermsChanges))
        data.addAll(fl54Accounting(idMain, this.fl54Accounting))

        return Pair(idMain, data)
    }

    private fun FlEvent2_3.tags(idFile: Number, taxPassport: String): Pair<Number, List<DataInfo>> {

        val data = ArrayList<DataInfo>()

        val idMain = findMainId(idFile, taxPassport, this.event, this.unicalId, this.eventDate, this.orderNum)

        data.addAll(fl17DealUid(idMain, this.fl17DealUid))
        data.addAll(fl18Deal(idMain, this.fl18Deal))
        data.addAll(fl19Amount(idMain, this.fl19Amount))
        data.addAll(fl191AmountInfo(idMain, this.fl19_1AmountInfoList))
        data.addAll(fl21PaymentTerms(idMain, this.fl21PaymentTerms))
        data.addAll(fl22TotalCost(idMain, this.fl22TotalCost))
        data.addAll(fl25262728Group(idMain, this.fl25_26_27_28Group))

        this.fl29MonthlyPayment?.let { data.addAll(fl29MonthlyPayment(idMain, it)) }

        this.fl29_1DebtBurdenInfo?.let { data.addAll(fl291DebtBurdenInfo(idMain, it)) }

        data.addAll(fl54Accounting(idMain, this.fl54Accounting))

        this.fl55Application?.let { data.addAll(fl55Application(idMain, it)) }

        data.addAll(fl56Participation(idMain, this.fl56Participation))

        return Pair(idMain, data)
    }

    private fun fl39Court(idMain: Number, fl39: Fl39Court): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_39_Court", "ActExist", fl39.getActExist()?:""),
            DataInfo(idMain, "FL_39_Court", "date", fl39.date?.value?:""),

            DataInfo(idMain, "FL_39_Court", "num", fl39.num?.value?:""),
            DataInfo(idMain, "FL_39_Court", "actResolution", fl39.actResolutionCode?.value?:""),
            DataInfo(idMain, "FL_39_Court", "lawsuitCode", fl39.lawsuitCode?.value?:""),
            DataInfo(idMain, "FL_39_Court", "sumTotal", fl39.sumTotal?.value?:""),
            DataInfo(idMain, "FL_39_Court", "info", fl39.info?.value?:""),
            DataInfo(idMain, "FL_39_Court", "isActStarted", fl39.isActStarted.toDb())
        )
    }

    private fun fl38ContractEnd(idMain: Number, fl38: Fl38ContractEnd): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_38_ContractEnd", "date", fl38.date?.value?:""),
            DataInfo(idMain, "FL_38_ContractEnd", "num", fl38.code?.value?:"")
        )
    }

    private fun fl3235Group(idMain: Number, fl3235: Fl32_35Group): List<DataInfo> {
        if(fl3235.propertyIdGroupFl32_35GroupList.isNullOrEmpty() ) return emptyList()

        val data = ArrayList<DataInfo>()

        for(pledge in fl3235.propertyIdGroupFl32_35GroupList) {

            data.addAll(pledge.info(idMain))
        }

        return data
    }

    private fun PropertyIdGroupFl32_35Group.info(idMain: Number): List<DataInfo> {

        val id = AfinaQuery.selectValue(SEL_PLEDGE, params = arrayOf(idMain, propertyId?.value)) as Number

        val sum = if(fl32Collateral.sumGroupFl32_35GroupList.isNullOrEmpty()) null else fl32Collateral.sumGroupFl32_35GroupList[0]

        val list = mutableListOf(
            DataInfo(idMain, "FL_32_35_Group", "propertyId", propertyId?.value?:"", id),

            DataInfo(idMain, "FL_32_35_Group", "date", fl32Collateral.date?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "code", fl32Collateral.code?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "collateralEndDate", fl32Collateral.collateralEndDate?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "collateralFactEndDate", fl32Collateral.collateralFactEndDate?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "endReason", fl32Collateral.endReason?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "contractTotalSum", fl32Collateral.contractTotalSum?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "contractCount", fl32Collateral.contractCount?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "location", fl32Collateral.okato?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "actualCost", fl32Collateral.actualCost?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "calcDate", fl32Collateral.calcDate?.value?:"", id),

            DataInfo(idMain, "FL_32_35_Group", "sum", sum?.sum?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "assessDate", sum?.assessDate?.value?:"", id),
            DataInfo(idMain, "FL_32_35_Group", "priceCode", sum?.priceCode?.value?:"", id)
        )

        list.addAll(insureInfo(idMain, id) )

        return list
    }

    private fun PropertyIdGroupFl32_35Group.insureInfo(idMain: Number, idPledge: Number): List<DataInfo> {

        if(fl35InsuranceList.isNullOrEmpty()) return emptyList()

        val insureList = fl35InsuranceList.filter { !(it.startDate?.value.isNullOrEmpty()) }

        val data = ArrayList<DataInfo>()

        for((index, insure) in insureList.withIndex())  {
            data += DataInfo(idMain, "FL_35_INSURANCE_$index", "startDate", insure.startDate!!.value, idPledge)

            data += DataInfo(idMain, "FL_35_INSURANCE_$index", "insuranceEndDate", insure.insuranceEndDate?.value?:"", idPledge)

            data += DataInfo(idMain, "FL_35_INSURANCE_$index", "insuranceFactEndDate", insure.insuranceFactEndDate?.value?:"", idPledge)

            data += DataInfo(idMain, "FL_35_INSURANCE_$index", "endCode", insure.endCode?.value?:"", idPledge)
        }

        return data
    }

    private fun fl33Warranty(idMain: Number, fl33: Fl33Warranty): List<DataInfo> {
        if(fl33.uidGroupFl33WarrantyList.isNullOrEmpty() ) return emptyList()

        val data = ArrayList<DataInfo>()

        for(guarant in fl33.uidGroupFl33WarrantyList) {

            data.addAll(guarant.info(idMain))
        }

        return data
    }

    private fun UidGroupFl33Warranty.info(idMain: Number): List<DataInfo> {

        val id = AfinaQuery.selectValue(SEL_GUARANT_BY_ID, params = arrayOf(uid?.value)) as Number

        return listOf(
            DataInfo(idMain, "FL_33_Warranty", "uid", uid?.value?:"", id),
            DataInfo(idMain, "FL_33_Warranty", "sum", sum?.value?:"", id),
            DataInfo(idMain, "FL_33_Warranty", "openDate", openDate?.value?:"", id),
            DataInfo(idMain, "FL_33_Warranty", "endDate", endDate?.value?:"", id),
            DataInfo(idMain, "FL_33_Warranty", "factEndDate", factEndDate?.value?:"", id),
            DataInfo(idMain, "FL_33_Warranty", "endCode", endCode?.value?:"", id),
        )
    }

    private fun fl24Fund(idMain: Number, fl24: Fl24Fund): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_24_Fund", "date", fl24.date?.value?:""),
            DataInfo(idMain, "FL_24_Fund", "num", fl24.num?.value?:""),
            DataInfo(idMain, "FL_24_Fund", "startSum", fl24.startSum?.value?:"")
        )
    }

    private fun fl25262728Group(idMain: Number, fl: Fl25_26_27_28Group): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_25_26_27_28_Group", "isLastPay", fl.isLastPayExist?.toDb()?:"" ),
            DataInfo(idMain, "FL_25_26_27_28_Group", "calcDate", fl.calcDate?.value?:""),

            DataInfo(idMain, "FL_25_Debt", "debtSum", fl.fl25Debt.debtSum?.value?:""),
            DataInfo(idMain, "FL_25_Debt", "debtMainSum", fl.fl25Debt.debtMainSum?.value?:""),
            DataInfo(idMain, "FL_25_Debt", "debtPercentSum", fl.fl25Debt.debtPercentSum?.value?:""),
            DataInfo(idMain, "FL_25_Debt", "debtOtherSum", fl.fl25Debt.debtOtherSum?.value?:""),

            DataInfo(idMain, "FL_26_DebtDue", "debtDueSum", fl.fl26DebtDue.debtDueSum?.value?:""),
            DataInfo(idMain, "FL_26_DebtDue", "debtDueMainSum", fl.fl26DebtDue.debtDueMainSum?.value?:""),
            DataInfo(idMain, "FL_26_DebtDue", "debtDuePercentSum", fl.fl26DebtDue.debtDuePercentSum?.value?:""),
            DataInfo(idMain, "FL_26_DebtDue", "debtDueOtherSum", fl.fl26DebtDue.debtDueOtherSum?.value?:""),
            DataInfo(idMain, "FL_26_DebtDue", "debtDueStartDate", fl.fl26DebtDue.debtDueStartDate?.value?:""),

            DataInfo(idMain, "FL_27_DebtOverdue", "debtOverdueSum", fl.fl27DebtOverdue.debtOverdueSum?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "debtOverdueMainSum", fl.fl27DebtOverdue.debtOverdueMainSum?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "debtOverduePercentSum", fl.fl27DebtOverdue.debtOverduePercentSum?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "debtOverdueOtherSum", fl.fl27DebtOverdue.debtOverdueOtherSum?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "debtOverdueStartDate", fl.fl27DebtOverdue.debtOverdueStartDate?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "mainMissDate", fl.fl27DebtOverdue.mainMissDate?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "percentMissDate", fl.fl27DebtOverdue.percentMissDate?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "missDuration", fl.fl27DebtOverdue.missDuration?.value?:""),
            DataInfo(idMain, "FL_27_DebtOverdue", "repaidMissDuration", fl.fl27DebtOverdue.repaidMissDuration?.value?:""),

            DataInfo(idMain, "FL_28_Payment", "paymentSum", fl.fl28Payment.paymentSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "paymentMainSum", fl.fl28Payment.paymentMainSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "paymentPercentSum", fl.fl28Payment.paymentPercentSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "paymentOtherSum", fl.fl28Payment.paymentOtherSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "totalSum", fl.fl28Payment.totalSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "totalMainSum", fl.fl28Payment.totalMainSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "totalPercentSum", fl.fl28Payment.totalPercentSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "totalOtherSum", fl.fl28Payment.totalOtherSum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "date", fl.fl28Payment.date?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "sizeCode", fl.fl28Payment.sizeCode?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "scheduleCode", fl.fl28Payment.scheduleCode?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "lastMissPaySum", fl.fl28Payment.lastMissPaySum?.value?:""),
            DataInfo(idMain, "FL_28_Payment", "paySum24", fl.fl28Payment.paySum24?.value?:"")
        )
    }


    private fun fl231ContractTermsChanges(idMain: Number, fl231: Fl23_1ContractTermsChanges): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_23_1_ContractTermsChanges", "termsChangeCode", fl231.termsChangeCode?.value?:""),
            DataInfo(idMain, "FL_23_1_ContractTermsChanges", "termsChangeDesc", fl231.termsChangeDesc?.value?:""),
            DataInfo(idMain, "FL_23_1_ContractTermsChanges", "changingDate", fl231.changingDate?.value?:"")
        )
    }

    private fun fl23ContractChanges(idMain: Number, fl23ContractChanges: Fl23ContractChanges): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_23_ContractChanges", "changeDate", fl23ContractChanges.changeDate?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "code", fl23ContractChanges.code?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "specialCode", fl23ContractChanges.specialCode?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "otherDesc", fl23ContractChanges.otherDesc?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "startDate", fl23ContractChanges.startDate?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "endDate", fl23ContractChanges.endDate?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "actualEndDate", fl23ContractChanges.actualEndDate?.value?:""),
            DataInfo(idMain, "FL_23_ContractChanges", "endCode", fl23ContractChanges.endCode?.value?:"")
        )
    }

    private fun fl29MonthlyPayment(idMain: Number, fl29MonthlyPayment: Fl29MonthlyPayment): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_29_MonthlyPayment", "sum", fl29MonthlyPayment.sum?.value?:""),
            DataInfo(idMain, "FL_29_MonthlyPayment", "calcDate", fl29MonthlyPayment.calcDate?.value?:""),
            DataInfo(idMain, "FL_29_MonthlyPayment", "sumTotal", fl29MonthlyPayment.sumTotal?.value?:"")
        )
    }

    private fun fl291DebtBurdenInfo(idMain: Number, fl291DebtBurdenInfo: Fl29_1DebtBurdenInfo): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "loadRange", fl291DebtBurdenInfo.loadRange?.value?:""),
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "loadCalcDate", fl291DebtBurdenInfo.loadCalcDate?.value?:""),
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "incomeInfo", fl291DebtBurdenInfo.incomeInfo?.value?:""),
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "incomeInfoSource", fl291DebtBurdenInfo.incomeInfoSourceList?.get(0)?.value?:""),
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "isLoadFact", fl291DebtBurdenInfo.isLoadFact?.toDb()?:""),
            DataInfo(idMain, "FL_29_1_DebtBurdenInfo", "isLoadCalculationFact", fl291DebtBurdenInfo.isLoadCalculationFact?.toDb()?:"")
        )
    }


    private fun fl56Participation(idMain: Number, fl56Participation: Fl56Participation): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_56_Participation", "role", fl56Participation.role?.value?:""),
            DataInfo(idMain, "FL_56_Participation", "kindCode", fl56Participation.kindCode?.value?:""),
            DataInfo(idMain, "FL_56_Participation", "uid", fl56Participation.uid?.value?:""),
            DataInfo(idMain, "FL_56_Participation", "fundDate", fl56Participation.fundDate?.value?:""),
            DataInfo(idMain, "FL_56_Participation", "isOverdue", if(fl56Participation.isOverdueExist){"1"}else{"0"}),
            DataInfo(idMain, "FL_56_Participation", "isStop", if(fl56Participation.isStopExist){"1"}else{"0"})
        )
    }

    private fun fl54Accounting(idMain: Number, fl54Accounting: Fl54Accounting): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_54_Accounting", "sum", fl54Accounting.sum?.value?:""),
            DataInfo(idMain, "FL_54_Accounting", "minInterest", fl54Accounting.minInterest?.value?:""),
            DataInfo(idMain, "FL_54_Accounting", "maxInterest", fl54Accounting.maxInterest?.value?:""),
            DataInfo(idMain, "FL_54_Accounting", "supportInfo", fl54Accounting.supportInfo?.value?:""),
            DataInfo(idMain, "FL_54_Accounting", "calcDate", fl54Accounting.calcDate?.value?:"")
        )
    }

    private fun fl22TotalCost(idMain: Number, fl22TotalCost: Fl22TotalCost?): List<DataInfo> {
        if(fl22TotalCost == null) return emptyList()

        return listOf(
            DataInfo(idMain, "FL_22_TotalCost", "minPercentCost", fl22TotalCost.minPercentCost?.value?:""),
            DataInfo(idMain, "FL_22_TotalCost", "mainPayDate", fl22TotalCost.minCost?.value?:""),
            DataInfo(idMain, "FL_22_TotalCost", "calcDate", fl22TotalCost.calcDate?.value?:""),
            DataInfo(idMain, "FL_22_TotalCost", "maxPercentCost", fl22TotalCost.maxPercentCost?.value?:""),
            DataInfo(idMain, "FL_22_TotalCost", "maxCost", fl22TotalCost.maxCost?.value?:"")
        )
    }

    private fun fl21PaymentTerms(idMain: Number, fl21PaymentTerms: Fl21PaymentTerms): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_21_PaymentTerms", "mainPaySum", fl21PaymentTerms.mainPaySum?.value?:""),
            DataInfo(idMain, "FL_21_PaymentTerms", "mainPayDate", fl21PaymentTerms.mainPayDate?.value?:""),
            DataInfo(idMain, "FL_21_PaymentTerms", "percentPaySum", fl21PaymentTerms.percentPaySum?.value?:""),
            DataInfo(idMain, "FL_21_PaymentTerms", "percentPayDate", fl21PaymentTerms.percentPayDate?.value?:""),
            DataInfo(idMain, "FL_21_PaymentTerms", "freqCode", fl21PaymentTerms.freqCode?.value?:""),
            DataInfo(idMain, "FL_21_PaymentTerms", "percentEndDate", fl21PaymentTerms.percentEndDate?.value?:"")
        )
    }

    private fun fl191AmountInfo(idMain: Number, fl191AmountInfoList: List<Fl19_1AmountInfo>?): List<DataInfo> {
        if(fl191AmountInfoList.isNullOrEmpty()) return emptyList()

        val fl191 = fl191AmountInfoList[0]

        return listOf(
            DataInfo(idMain, "FL_19_1_AmountInfo", "securitySum", fl191.securitySum?.value?:""),
            DataInfo(idMain, "FL_19_1_AmountInfo", "securityTypeCode", fl191.securityTypeCode?.value?:""),
            DataInfo(idMain, "FL_19_1_AmountInfo", "calcDate", fl191.calcDate?.value?:""),
            DataInfo(idMain, "FL_19_1_AmountInfo", "securityUid", fl191.securityUid?.value?:""),
            DataInfo(idMain, "FL_19_1_AmountInfo", "liabilityLimit", fl191.liabilityLimit?.value?:"")
        )
    }

    private fun fl19Amount(idMain: Number, fl19Amount: Fl19Amount): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_19_Amount", "sum", fl19Amount.sum?.value?:""),
            DataInfo(idMain, "FL_19_Amount", "calcDate", fl19Amount.calcDate?.value?:"")
        )
    }

    private fun fl18Deal(idMain: Number, fl18Deal: Fl18Deal): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_18_Deal", "role", fl18Deal.role?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "code", fl18Deal.code?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "kindCode", fl18Deal.kindCode?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "purposeCode", fl18Deal.purposeCode?.value?:""),

            DataInfo(idMain, "FL_18_Deal", "endDate", fl18Deal.endDate?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "creditLineCode", fl18Deal.creditLineCode?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "startDate", fl18Deal.startDate?.value?:""),
            DataInfo(idMain, "FL_18_Deal", "isConsumer", if(fl18Deal.isConsumer){"1"}else{"0"})
        )
    }

    private fun fl17DealUid(idMain: Number, fl17DealUid: Fl17DealUid): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_17_DealUid", "uid", fl17DealUid.uid?.value?:""),
            DataInfo(idMain, "FL_17_DealUid", "num", fl17DealUid.num?.value?:""),
            DataInfo(idMain, "FL_17_DealUid", "refUid", fl17DealUid.refUid?.value?:""),
            DataInfo(idMain, "FL_17_DealUid", "openDate", fl17DealUid.openDate?.value?:"")
        )
    }

    private fun fl11IndividualEntrepreneur(idMain: Number, fl11IndividualEntrepreneur: Fl11IndividualEntrepreneur): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_11_IndividualEntrepreneur", "regNum", fl11IndividualEntrepreneur.regNum?.value?:""),
            DataInfo(idMain, "FL_11_IndividualEntrepreneur", "date", fl11IndividualEntrepreneur.date?.value?:"")
        )
    }

    private fun fl10Contact(idMain: Number, fl10Contact: Fl10Contact): List<DataInfo> {

        return listOf(
            DataInfo(idMain, "FL_10_Contact", "phone",
                fl10Contact.phoneGroupFl10ContactList?.joinToString(";"){ it.phone.value } ?:""),
            DataInfo(idMain, "FL_10_Contact", "email",
                fl10Contact.emailList?.joinToString(";") { it.value } ?:"")
        )
    }

    private fun fl9AddrFact(idMain: Number, fl9AddrFact: Fl9AddrFact): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_9_AddrFact", "postCode", fl9AddrFact.postCode?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "regStateNum", fl9AddrFact.regStateNum?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "locationCode", fl9AddrFact.okato?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "street", fl9AddrFact.street?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "house", fl9AddrFact.house?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "estate", fl9AddrFact.estate?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "block", fl9AddrFact.block?.value?:""),
            DataInfo(idMain, "FL_9_AddrFact", "build", fl9AddrFact.build?.value?:"")
        )
    }

    private fun fl8AddrReg(idMain: Number, fl8AddrReg: Fl8AddrReg): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_8_AddrReg", "code", fl8AddrReg.code?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "postCode", fl8AddrReg.postCode?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "regStateNum", fl8AddrReg.regStateNum?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "locationCode", fl8AddrReg.okato?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "street", fl8AddrReg.street?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "house", fl8AddrReg.house?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "estate", fl8AddrReg.estate?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "block", fl8AddrReg.block?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "build", fl8AddrReg.build?.value?:""),

            DataInfo(idMain, "FL_8_AddrReg", "date", fl8AddrReg.dateRegister?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "dept", fl8AddrReg.deptName?.value?:""),
            DataInfo(idMain, "FL_8_AddrReg", "deptCode", fl8AddrReg.deptCode?.value?:"")
        )
    }

    private fun fl57Reject(idMain: Number, fl57Reject: Fl57Reject): List<DataInfo> {
        return listOf(
            DataInfo(idMain, "FL_57_Reject", "rejectDate", fl57Reject.rejectDate?.value?:""),
            DataInfo(idMain, "FL_57_Reject", "rejectCode", fl57Reject.rejectCode?.value?:"")
        )
    }

    private fun title(idMain: Number, title: SubjectTitleDataFl): List<DataInfo> {
        val data = ArrayList<DataInfo>()

        data += DataInfo(idMain, "FL_1_4_Group", "lastName", title.fl1_4Group.fio.lastName?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "firstName", title.fl1_4Group.fio.firstName?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "middleName", title.fl1_4Group.fio.middleName?.value?:"")

        data += DataInfo(idMain, "FL_1_4_Group", "docCode", title.fl1_4Group.fl4Doc.docCode?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "docNum", title.fl1_4Group.fl4Doc.docNum?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "docSeries", title.fl1_4Group.fl4Doc.docSeries?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "issueDate", title.fl1_4Group.fl4Doc.issueDate?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "docIssuer", title.fl1_4Group.fl4Doc.docIssuer?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "deptCode", title.fl1_4Group.fl4Doc.deptCode?.value?:"")
        data += DataInfo(idMain, "FL_1_4_Group", "foreignerCode", title.fl1_4Group.fl4Doc.foreignerCode?.value?:"")

        data += DataInfo(idMain, "FL_2_5_Group", "lastName", title.fl2_5Group.flPrevName.lastName?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "firstName", title.fl2_5Group.flPrevName.firstName?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "middleName", title.fl2_5Group.flPrevName.middleName?.value?:"")

        data += DataInfo(idMain, "FL_2_5_Group", "docCode", title.fl2_5Group.fl5PrevDoc.docCode?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "docNum", title.fl2_5Group.fl5PrevDoc.docNum?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "docSeries", title.fl2_5Group.fl5PrevDoc.docSeries?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "issueDate", title.fl2_5Group.fl5PrevDoc.issueDate?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "docIssuer", title.fl2_5Group.fl5PrevDoc.docIssuer?.value?:"")
        data += DataInfo(idMain, "FL_2_5_Group", "deptCode", title.fl2_5Group.fl5PrevDoc.deptCode?.value?:"")

        data += DataInfo(idMain, "FL_6_Tax", "regNum", title.fl6Tax?.regNum?.value?:"")
        data += DataInfo(idMain, "FL_6_Tax", "taxNum", title.fl6Tax?.taxNumGroup?.taxNum?.value?:"")

        data += DataInfo(idMain, "FL_7_Social", "socialNum", title.fl7Social?.socialNum?.value?:"")

        return data
    }

    private fun fl55Application(idMain: Number, fl55Application: Fl55Application): List<DataInfo> {
        return listOf(
        DataInfo(idMain, "FL_55_Application", "role", fl55Application.role?.value?:""),
        DataInfo(idMain, "FL_55_Application", "sum", fl55Application.sum?.value?:""),
        DataInfo(idMain, "FL_55_Application", "uid", fl55Application.uid?.value?:""),
        DataInfo(idMain, "FL_55_Application", "applicationDate", fl55Application.applicationDate?.value?:""),
        DataInfo(idMain, "FL_55_Application", "approvalEndDate", fl55Application.approvalEndDate?.value?:""),
        DataInfo(idMain, "FL_55_Application", "stageEndDate", fl55Application.stageEndDate?.value?:""),
        DataInfo(idMain, "FL_55_Application", "purposeCode", fl55Application.purposeCode?.value?:""),
        DataInfo(idMain, "FL_55_Application", "stageCode", fl55Application.stageCode?.value?:""),
        DataInfo(idMain, "FL_55_Application", "stageDate", fl55Application.stageDate?.value?:""),
        DataInfo(idMain, "FL_55_Application", "num", fl55Application.num?.value?:""),
        DataInfo(idMain, "FL_55_Application", "loanSum", fl55Application.loanSum?.value?:"")
        )
    }

    private fun findMainId(idFile: Number, taxPassport: String, event: String,
                           unicalUid: String?, eventDateXml: String, orderNum: Int): Number {

        logger.error("idFile=$idFile")
        logger.error("taxPassport=$taxPassport")
        logger.error("event=$event")
        logger.error("unicalUid=$unicalUid")
        logger.error("eventDateXml=$eventDateXml")

        val id = when {
            (unicalUid == null) && (taxPassport.length == 13) ->
                AfinaQuery.selectValue(SEL_MAIN_BY_INN,
                    params = arrayOf(idFile, event, eventDateXml.xmlDateToTimestamp(),
                        taxPassport.substring(1), taxPassport.substring(0..0)))

            (unicalUid == null) && (taxPassport.length != 13) ->
                AfinaQuery.selectValue(SEL_MAIN_BY_PASSPORT,
                    params = arrayOf(idFile, taxPassport.substring(0..3),
                        taxPassport.substring(4),
                        event, eventDateXml.xmlDateToTimestamp() ))

            (unicalUid != null) &&
            (event in listOf("1.1", "1.2", "1.3")) &&
            (taxPassport.length == 13) ->
                AfinaQuery.selectValue(SEL_MAIN_BY_UID_INN,
                    params = arrayOf(idFile, unicalUid, event, eventDateXml.xmlDateToTimestamp(),
                        taxPassport.substring(1), taxPassport.substring(0..0)))

            (unicalUid != null) &&
                    (event in listOf("1.1", "1.2", "1.3")) &&
                    (taxPassport.length != 13) ->
                AfinaQuery.selectValue(SEL_MAIN_BY_UID_PASSPORT,
                    params = arrayOf(idFile, unicalUid, event, eventDateXml.xmlDateToTimestamp(),
                        taxPassport.substring(0..3), taxPassport.substring(4)))

            else -> AfinaQuery.selectValue(SEL_MAIN_BY_UID,
                params = arrayOf(idFile, unicalUid, event, eventDateXml.xmlDateToTimestamp(), ""))
        } as Number

        AfinaQuery.execute(UPDATE_MAIN_ORDER, params = arrayOf(orderNum, id))

        return id
    }

    private fun loadFromXml(file: File): MainDocument {

        val xmlLoader = XmlGutDfLoader<MainDocument>()

        return xmlLoader.load(file)
    }
}

const val UPDATE_MAIN_ORDER = "update od.PTKB_RUTDF_MAIN set ORDER_NUM_INFILE = ? where ID = ?"

private const val SEL_MAIN_BY_PASSPORT = "select od.PTKB_RUTDF.getMainByPassport(?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_UID = "select od.PTKB_RUTDF.getMainByGuid(?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_INN = "select od.PTKB_RUTDF.getMainByInnIpOrPhysic(?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_UID_INN = "select od.PTKB_RUTDF.getMainByGuidAndInn(?, ?, ?, ?, ?, ?) from dual"

private const val SEL_MAIN_BY_UID_PASSPORT = "select od.PTKB_RUTDF.getMainByGuidAndPassport(?, ?, ?, ?, ?, ?) from dual"


fun String.xmlToLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

fun String.xmlDateToTimestamp(): Timestamp = xmlToLocalDate().toTimestamp()

class DataInfo(val idMain: Number, val block: String, val tag: String, val value: String, val addId: Number? = null)
private val logger = LoggerFactory.getLogger(GutdfLoaderFile::class.java)

fun ArrayList<DataInfo>.saveAll(idFile: Number) {

    if(size == 0) return

    val session = AfinaQuery.uniqueSession()

    try {
        AfinaQuery.execute(DEL_FILE_DATA,  params = arrayOf(idFile), sessionSetting = session)

        for (data in this) {

            if(data.addId == null) {

                AfinaQuery.execute(EXEC_SAVE_DATA, sessionSetting = session, params = arrayOf(data.idMain,
                    data.block.uppercase(Locale.getDefault()), data.tag.uppercase(Locale.getDefault()), data.value))
            } else {

                AfinaQuery.execute(EXEC_SAVE_DATA_ADD, sessionSetting = session, params = arrayOf(data.idMain,
                    data.block.uppercase(Locale.getDefault()), data.tag.uppercase(Locale.getDefault()), data.value, data.addId))
            }
        }

        AfinaQuery.commitFree(session)

    } catch (e: Exception) {
        logger.error("fail saveAll", e)

        AfinaQuery.rollbackFree(session)

        throw Exception(e)
    }
}

fun Boolean.toDb(): String = if(this){"1"}else{"0"}

private const val EXEC_SAVE_DATA = "insert into od.PTKB_GUTDF_VALUES (ID_MAIN, BLOCK, TAG, VALUE) values (?, ?, ?, ?)"

private const val EXEC_SAVE_DATA_ADD = "insert into od.PTKB_GUTDF_VALUES (ID_MAIN, BLOCK, TAG, VALUE, ADVANCE_ID) values (?, ?, ?, ?, ?)"

const val SEL_GUARANT_BY_ID = "select od.PTKB_GUTDF.getGuarantByUid( ? ) from dual"

const val SEL_PLEDGE = "select od.PTKB_GUTDF.getPledgeByPropertyId( ?, ? ) from dual"

private const val DEL_FILE_DATA = "{ call od.PTKB_RUTDF.deleteAllValuesByFile( ? ) }"

