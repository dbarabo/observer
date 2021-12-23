package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r3.DataForm310R3
import java.sql.Date

internal fun Data310Form.addSection3(dateReport: java.util.Date) {

    val params = arrayOf<Any?>( Date(dateReport.time))

    val section3Data = AfinaQuery.selectCursor(SELECT_R3, params)

    if(section3Data.isEmpty()) {
        this.sectionsR3 = emptyList()
        return
    }

    for(row in section3Data) {

        toRowSection3(row).apply {
            addSection3(
                DataForm310R3(idCodeSubjectPledge, codeCountryOksm, codeFias, codeOkato,
                    postIndex, street, houseNumber, housing, letter,
                    apartmentNumber, fullAddress) )
        }
    }
}

private const val SELECT_R3 = "{ ? = call od.PTKB_FORM_310.getSection3( ? ) }"

private data class RowSection3(val idCodeSubjectPledge: Number, val codeCountryOksm: Number,
                               val codeFias: String?, val codeOkato: String?,
                               val postIndex: String?, val street: String?,
                               val houseNumber: String?, val housing: String?,
                               val letter: String?, val apartmentNumber: String?,
                               val fullAddress: String?)

private fun toRowSection3(row: Array<Any?>): RowSection3 {

    return RowSection3(idCodeSubjectPledge = row[0] as Number, codeCountryOksm = row[1] as Number,
        codeFias = (row[2] as? String)?.takeIf { it.length == "f77948dc-7bc8-42cb-979e-2c958d162d6".length },
        codeOkato = row[3] as? String,
        postIndex = row[4] as? String, street = row[5] as? String,
        houseNumber = row[6] as? String, housing = row[7] as? String,
        letter = row[8] as? String, apartmentNumber = row[9] as? String,
        fullAddress = row[10] as? String)
}