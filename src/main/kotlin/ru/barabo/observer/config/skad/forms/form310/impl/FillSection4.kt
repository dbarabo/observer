package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r4.*
import java.sql.Date

internal fun Data310Form.addSection4(dateReport: java.util.Date) {

    val params = arrayOf<Any?>( Date(dateReport.time))

    val section4Data = AfinaQuery.selectCursor(SELECT_R4, params)

    if(section4Data.isEmpty()) {
        this.sectionsR4 = emptyList()
        return
    }

    for(row in section4Data) {

        toRowSection4(row).apply {

            var subSectionR42: SubSectionR42? = null
            var subSectionR43: SubSectionR43? = null
            var subSectionR44: SubSectionR44? = null
            var subSectionR45: SubSectionR45? = null
            var subSectionR46: SubSectionR46? = null
            var subSectionR47: SubSectionR47? = null
            var subSectionR48: SubSectionR48? = null
            var subSectionR49: SubSectionR49? = null
            var subSectionR410: SubSectionR410? = null
            var subSectionR411: SubSectionR411? = null
            var subSectionR412: SubSectionR412? = null

            var subSectionR413: SubSectionR413? = null
            var subSectionR414: SubSectionR414? = null
            var subSectionR415: SubSectionR415? = null
            var subSectionR416: SubSectionR416? = null
            var subSectionR417: SubSectionR417? = null
            var subSectionR418: SubSectionR418? = null
            var subSectionR419: SubSectionR419? = null
            var subSectionR420: SubSectionR420? = null
            var subSectionR421: SubSectionR421? = null

            when(typePledge) {
                TypePledge.RealEstate -> {
                    subSectionR42 = SubSectionR42(propertyType?.toString(), cadastralNumber,
                        conditionalNumber, purpose, name, functionalGroup, shareSizePercent, areaSqM, codeLandCategory,
                        permittedUseLandPlot, codePledgerRight, expiryDateLease, typeConstruction)
                }
                TypePledge.Transport -> {
                    subSectionR43 = SubSectionR43(category?.toString(), vinCar, idSelfPropelledCar, yearIssue, brand, model, frameNumber,
                        enginePowerHP?.toInt()?.toString(), enginePowerkW?.toInt()?.toString(), engineDisplacementSm3)
                }
                TypePledge.Engine -> {
                    subSectionR44 = SubSectionR44(name, factoryNumber, inventoryNumber, yearIssue, brand, model, group)
                }
                TypePledge.AirCraft -> {
                    subSectionR45 = SubSectionR45(name, factoryNumber, inventoryNumber, yearIssue, brand, group)
                }
                TypePledge.Float -> {
                    subSectionR46 = SubSectionR46(groupFloat, numberImo, numberMmsi, classModel, registerNumber, yearIssue, name)
                }
                TypePledge.Train -> {
                    subSectionR47 = SubSectionR47(factoryNumber, yearIssue, typeUnitRailway , classModel, registerNumber)
                }
                TypePledge.SpaceShip -> {
                    subSectionR48 = SubSectionR48(name, factoryNumber, yearIssue)
                }
                TypePledge.GoodsCirculation -> {
                    subSectionR49 = SubSectionR49(typeGoodsCirculation)
                }
                TypePledge.FutureHarvest -> {
                    throw Exception("Будущего урожая не будет!")
                }
                TypePledge.Metal -> {
                    subSectionR411 = SubSectionR411(metalType, weightG?.toString(), numberIngots?.toString())
                }
                TypePledge.IntellectualProperty -> {
                    subSectionR412 = SubSectionR412(typeIntellectualProperty, documentNumber)
                }
                TypePledge.CollateralAccount -> {
                    subSectionR413 = SubSectionR413(collateralAccountCode, infoAmountMoney, amountMoney)
                }
                TypePledge.ShareCapital -> {
                    subSectionR414 = SubSectionR414(idSubjectCode, authorizedCapitalPercent)
                }
                TypePledge.Bill -> {
                    subSectionR415 = SubSectionR415(idCodeBill, typeBill, signBill, idSubjectCode, codeLocation)
                }
                TypePledge.GradeSecurity -> {
                    subSectionR416 = SubSectionR416(typeIssueGradeSecurity, countSecurity, codeIsin, stateRegistrationNumber, idNumberSecurity)
                }
                TypePledge.UnitsMutualInvestmentFund -> {
                    subSectionR417 = SubSectionR417(countSecurity, codeIsin, stateRegistrationNumber)
                }
                TypePledge.Certificates -> {
                    subSectionR418 = SubSectionR418(countSecurity, codeIsin, stateRegistrationNumber,
                        individualDesignation, expireTrustManage)
                }
                TypePledge.Securities -> {
                    subSectionR419 = SubSectionR419(typeSecurity, securitySeries, securityNumber)
                }
                TypePledge.PropertyRights -> {
                    subSectionR420 = SubSectionR420(typePropertyRights, name)
                }
                TypePledge.Others -> {
                    subSectionR421 = SubSectionR421(typeOthers, name)
                }
            }

            val rowR4 = DataForm310R4(idCodeSubjectPledge, accountCode, codePledgedProperty, qualityCategory,
                idCodeGroup, collateralSign,
                subSectionR42, subSectionR43, subSectionR44, subSectionR45, subSectionR46, subSectionR47, subSectionR48,
                subSectionR49, subSectionR410, subSectionR411, subSectionR412, subSectionR413, subSectionR414, subSectionR415,
                subSectionR416, subSectionR417, subSectionR418, subSectionR419, subSectionR420, subSectionR421)

            addSection4( rowR4 )
        }
    }
}

private const val SELECT_R4 = "{ ? = call od.PTKB_FORM_310.getSection4( ? ) }"

private enum class TypePledge(val order: Int) {

    RealEstate(1),
    Transport(2),
    Engine(3),
    AirCraft(4),
    Float(5),
    Train(6),
    SpaceShip(7),
    GoodsCirculation(8),
    FutureHarvest(9),
    Metal(10),
    IntellectualProperty(11),
    CollateralAccount(12),
    ShareCapital(13),
    Bill(14),
    GradeSecurity(15),
    UnitsMutualInvestmentFund(16),
    Certificates(17),
    Securities(18),
    PropertyRights(19),
    Others(20);
}

private fun typePledgeByOrder(order: Int): TypePledge =
    TypePledge.values().firstOrNull { it.order == order } ?: throw Exception("TypePledge with order=$order not found")


private data class RowSection4(val idCodeSubjectPledge: Number, val accountCode: String, val codePledgedProperty: Number,
                               val qualityCategory: Number?, val idCodeGroup: Number?, val collateralSign: Int,

                               val typePledge: TypePledge,
                               val propertyType: Number?,
                               val cadastralNumber: String?,
                               val conditionalNumber: String?,
                               val purpose: String?,
                               val name: String?,
                               val functionalGroup: String?,
                               val shareSizePercent: Number?,
                               val areaSqM: Number?,
                               val codeLandCategory: String?,
                               val permittedUseLandPlot: String?,
                               val codePledgerRight: String?,
                               val expiryDateLease: Date?,
                               val typeConstruction: String?,

                               val category: String?,
                               val vinCar: String?,
                               val idSelfPropelledCar: String?,
                               val yearIssue: Int?,
                               val brand: String?,
                               val model: String?,
                               val frameNumber: String?,
                               val enginePowerHP: Number?,
                               val enginePowerkW: Number?,
                               val engineDisplacementSm3: Number?,

                               val factoryNumber: String?,
                               val inventoryNumber: String?,
                               val group: String?,

                               val groupFloat: String?,
                               val numberImo: String?,
                               val numberMmsi: String?,
                               val classModel: String?,
                               val registerNumber: String?,

                               val typeUnitRailway: String?,

                               val typeGoodsCirculation: String?,

                               val metalType: String?,
                               val weightG: Int?,
                               val numberIngots: Int?,

                               val typeIntellectualProperty: String?,
                               val documentNumber: String?,

                               val collateralAccountCode: String?,
                               val infoAmountMoney: String?,
                               val amountMoney: Number?,

                               val idSubjectCode: String?,
                               val authorizedCapitalPercent: Number?,

                               val idCodeBill: String?,
                               val typeBill: String?,
                               val signBill: String?,
                               val codeLocation: String?,

                               val typeIssueGradeSecurity: String?,
                               val countSecurity: Int?,
                               val codeIsin: String?,
                               val stateRegistrationNumber: String?,
                               val idNumberSecurity: String?,

                               val individualDesignation: String?,
                               val expireTrustManage: Date?,

                               val typeSecurity: String?,
                               val securitySeries: String?,
                               val securityNumber: String?,

                               val typePropertyRights: String?,

                               val typeOthers: String?

)

private fun toRowSection4(row: Array<Any?>): RowSection4 {

    return RowSection4(
        idCodeSubjectPledge = row[0] as Number,
        accountCode = row[1] as String,
        codePledgedProperty = row[2] as Number,
        qualityCategory = row[3] as? Number,
        idCodeGroup = row[4] as? Number,
        collateralSign = (row[5] as Number).toInt(),

        typePledge = typePledgeByOrder((row[6] as Number).toInt()),

        propertyType = row[7] as? Number,
        cadastralNumber = row[8] as? String,
        conditionalNumber = row[9] as? String,
        purpose = row[10] as? String,
        name = row[11] as? String,
        functionalGroup = row[12] as? String,
        shareSizePercent = row[13] as? Number,
        areaSqM = row[14] as? Number,
        codeLandCategory = row[15] as? String,
        permittedUseLandPlot = row[16] as? String,
        codePledgerRight = row[17] as? String,
        expiryDateLease = row[18] as? Date,
        typeConstruction = row[19] as? String,

        category = row[20] as? String,
        vinCar = row[21] as? String,
        idSelfPropelledCar = row[22] as? String,
        yearIssue = (row[23] as? Number)?.toInt(),
        brand = row[24] as? String,
        model = row[25] as? String,
        frameNumber = row[26] as? String,
        enginePowerHP = row[27] as? Number,
        enginePowerkW = row[28] as? Number,
        engineDisplacementSm3 = row[29] as? Number,

        factoryNumber = row[30] as? String,
        inventoryNumber = row[31] as? String,
        group = row[32] as? String,

        groupFloat = row[33] as? String,
        numberImo = row[34] as? String,
        numberMmsi = row[35] as? String,
        classModel = row[36] as? String,
        registerNumber = row[37] as? String,

        typeUnitRailway = row[38] as? String,

        typeGoodsCirculation = row[39] as? String,

        metalType = row[40] as? String,
        weightG = (row[41] as? Number)?.toInt(),
        numberIngots = (row[42] as? Number)?.toInt(),

        typeIntellectualProperty = row[43] as? String,
        documentNumber = row[44] as? String,

        collateralAccountCode = row[45] as? String,
        infoAmountMoney = row[46] as? String,
        amountMoney = row[47] as? Number,

        idSubjectCode = row[48] as? String,
        authorizedCapitalPercent = row[49] as? Number,

        idCodeBill = row[50] as? String,
        typeBill = row[51] as? String,
        signBill = row[52] as? String,
        codeLocation = row[53] as? String,

        typeIssueGradeSecurity = row[54] as? String,
        countSecurity = (row[55] as? Number)?.toInt(),
        codeIsin = row[56] as? String,
        stateRegistrationNumber = row[57] as? String,
        idNumberSecurity = row[58] as? String,

        individualDesignation = row[59] as? String,
        expireTrustManage = row[60] as? Date,

        typeSecurity = row[61] as? String,
        securitySeries = row[62] as? String,
        securityNumber = row[63] as? String,

        typePropertyRights = row[64] as? String,

        typeOthers = row[65] as? String
    )
}