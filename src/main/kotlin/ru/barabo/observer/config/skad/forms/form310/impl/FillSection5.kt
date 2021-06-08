package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r5.DataForm310R5
import ru.barabo.observer.config.task.form310.section.r5.SubSectionR51
import ru.barabo.observer.config.task.form310.section.r5.SubSectionR52
import ru.barabo.observer.config.task.form310.section.r5.SubSectionR53
import java.sql.Date

internal fun Data310Form.addSection5(dateReport: java.util.Date) {

    val params = arrayOf<Any?>( Date(dateReport.time))

    val isNotExists = (AfinaQuery.selectValue(SELECT_IS_EXISTS, params) as Number).toInt() == 0

    if(isNotExists) {
        this.sectionsR5 = emptyList()
        return
    }

    addSection5R1(params)

    addSection5R2(params)

    addSection5R3(params)

}

private fun Data310Form.addSection5R1(params: Array<Any?>) {
    val section5R1 = AfinaQuery.selectCursor(SELECT_R5_1, params)
    for(row in section5R1) {

        toRowSection5R1(row).apply {
            addSection5(
                DataForm310R5(idCodeSubjectPledge,
                    SubSectionR51(codeCountryOksm, fullName, shortName, ogrn, inn, kio, tin, lei, null)
                ) )
        }
    }
}

private fun Data310Form.addSection5R2(params: Array<Any?>) {
    val section5R2 = AfinaQuery.selectCursor(SELECT_R5_2, params)
    for(row in section5R2) {

        toRowSection5R2(row).apply {
            addSection5(
                DataForm310R5(idCodeSubjectPledge,
                    SubSectionR52(typePhysic, codeCountryOksm, surname, firstName, middleName, inn, ogrnIp,
                        codeDocument, seriesDocument, numberDocument) ) )
        }
    }
}

private fun Data310Form.addSection5R3(params: Array<Any?>) {
    val section5R3 = AfinaQuery.selectCursor(SELECT_R5_3, params)
    for(row in section5R3) {

        toRowSection5R3(row).apply {
            addSection5(
                DataForm310R5(idCodeSubjectPledge,
                    SubSectionR53(codeCountryOksm, fullName, registrationNumberBank, bik, swift, tin, lei,
                        registrationNumberCountry) ) )
        }
    }
}

private const val SELECT_IS_EXISTS = "select od.PTKB_FORM_310.isExistsSection5( ? ) from dual"

private const val SELECT_R5_1 = "{ ? = call od.PTKB_FORM_310.getSection5_1( ? ) }"

private const val SELECT_R5_2 = "{ ? = call od.PTKB_FORM_310.getSection5_2( ? ) }"

private const val SELECT_R5_3 = "{ ? = call od.PTKB_FORM_310.getSection5_3( ? ) }"

private data class RowSection5R3(val idCodeSubjectPledge: Number,
                                 val codeCountryOksm: Number,
                                 val fullName: String,
                                 val registrationNumberBank: String?,
                                 val bik: String?,
                                 val swift: String?,
                                 val tin: String?,
                                 val lei: String?,
                                 val registrationNumberCountry: String?)

private fun toRowSection5R3(row: Array<Any?>): RowSection5R3 {

    return RowSection5R3(
        idCodeSubjectPledge = row[0] as Number,
        codeCountryOksm = row[1] as Number,
        fullName = row[2] as String,
        registrationNumberBank = row[3] as? String,
        bik = row[4] as? String,
        swift = row[5] as? String,
        tin = row[6] as? String,
        lei = row[7] as? String,
        registrationNumberCountry = null)
}

private data class RowSection5R2(val idCodeSubjectPledge: Number,
                                 val typePhysic: String?,
                                 val codeCountryOksm: Number,
                                 val surname: String?,
                                 val firstName: String?,
                                 val middleName: String?,
                                 val inn: String?,
                                 val ogrnIp: String?,
                                 val codeDocument: String?,
                                 val seriesDocument: String?,
                                 val numberDocument: String?)

private fun toRowSection5R2(row: Array<Any?>): RowSection5R2 {

    return RowSection5R2(
        idCodeSubjectPledge = row[0] as Number,
        typePhysic = row[1] as? String,
        codeCountryOksm = row[2] as Number,
        surname = row[3] as? String,
        firstName = row[4] as? String,
        middleName = row[5] as? String,
        inn = row[6] as? String,
        ogrnIp = row[7] as? String,
        codeDocument = row[8] as? String,
        seriesDocument = row[9] as? String,
        numberDocument = row[10] as? String)
}

private data class RowSection5R1(val idCodeSubjectPledge: Number,
                                 val codeCountryOksm: Number,
                                 val fullName: String?,
                                 val shortName: String?,
                                 val ogrn: String?,
                                 val inn: String?,
                                 val kio: String?,
                                 val tin: String?,
                                 val lei: String?)

private fun toRowSection5R1(row: Array<Any?>): RowSection5R1 {

    return RowSection5R1(
        idCodeSubjectPledge = row[0] as Number,
        codeCountryOksm = row[1] as Number,
        fullName = row[2] as? String,
        shortName = row[3] as? String,
        ogrn = row[4] as? String,
        inn = row[5] as? String,
        kio = row[6] as? String,
        tin = row[7] as? String,
        lei = row[8] as? String)
}