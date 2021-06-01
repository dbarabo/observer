package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r2.DataForm310R2
import java.util.*

internal fun Data310Form.addSection2(dateReport: Date) {

    val params = arrayOf<Any?>( java.sql.Date(dateReport.time))

    val section2Data = AfinaQuery.selectCursor(SELECT_R2, params)

    if(section2Data.isEmpty()) {
        this.sectionsR2 = emptyList()
        return
    }

    for(row in section2Data) {

        toRowSection2(row).apply {
            addSection2(DataForm310R2(idCodeSubjectPledge, idCodeGroup, amountSubject, dateDefineAmountSubject,
                amountMarket, dateDefineAmountMarket, amountCadastral, dateDefineAmountCadastral, amountLiquidation,
                dateDefineAmountLiquidation, amountInvestment, dateDefineAmountInvestment, costAgreed, dateDefineCostAgreed) )
        }
    }
}

private const val SELECT_R2 = "{ ? = call od.PTKB_FORM_310.getSection2( ? ) }"

private data class RowSection2(val idCodeSubjectPledge: Number, val idCodeGroup: Number?,
                               val amountSubject: Number?, val dateDefineAmountSubject: Date?,
                               val amountMarket: Number?, val dateDefineAmountMarket: Date?,
                               val amountCadastral: Number?, val dateDefineAmountCadastral: Date?,
                               val amountLiquidation: Number?, val dateDefineAmountLiquidation: Date?,
                               val amountInvestment: Number?, val  dateDefineAmountInvestment: Date?,
                               val costAgreed: Number?, val dateDefineCostAgreed: Date?)

private fun toRowSection2(row: Array<Any?>): RowSection2 {

    return RowSection2(idCodeSubjectPledge = row[0] as Number, idCodeGroup = row[1] as? Number,
        amountSubject = row[2] as? Number, dateDefineAmountSubject = row[3] as? Date,
        amountMarket = row[4] as? Number, dateDefineAmountMarket = row[5] as? Date,
        amountCadastral = row[6] as? Number, dateDefineAmountCadastral = row[7] as? Date,
        amountLiquidation = row[8] as? Number, dateDefineAmountLiquidation = row[9] as? Date,
        amountInvestment = row[10] as? Number, dateDefineAmountInvestment = row[11] as? Date,
        costAgreed = row[12] as? Number, dateDefineCostAgreed = row[13] as? Date
    )
}