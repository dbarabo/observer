package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r1.DataForm310R1
import ru.barabo.observer.config.task.form310.section.r1.SubSectionR1123
import java.util.*

fun checkCreateData310Form(dateReport: Date): Data310Form? {

    val params = arrayOf<Any?>( java.sql.Date(dateReport.time))

    val section1Data = AfinaQuery.selectCursor(SELECT_R1, params).takeIf { it.isNotEmpty() } ?: return null

    val data310Form = Data310Form()

    for(row in section1Data) {

        val section1Row = toRowSection1(row)

        data310Form.addSection1(section1Row, dateReport)
    }

    data310Form.addSection15(dateReport)

    data310Form.addSection16(dateReport)

    data310Form.addSection2(dateReport)

    data310Form.addSection3(dateReport)

    data310Form.addSection4(dateReport)

    data310Form.addSection5(dateReport)

    data310Form.addSection6(dateReport)

    return data310Form
}

private fun Data310Form.addSection1(rowSection1: RowSection1, dateReport: Date) {
    if(rowSection1.isClosedPawnPact) {
        addClosedPawnPact(rowSection1)
    } else {
        addOpenPawnPact(rowSection1, dateReport)
    }
}

private fun Data310Form.addOpenPawnPact(rowSection1: RowSection1, dateReport: Date) {

    val pawnGoodsIdList = AfinaQuery.selectCursor(SELECT_R1_3,
        arrayOf(rowSection1.pawnPactId, java.sql.Date(dateReport.time)) )
        .map { it[0].toString() }

    val subSectionR1123 = SubSectionR1123(
        listOf(rowSection1.loanId.toString()), rowSection1.pawnpactOpen,
        rowSection1.pawnpactLabel, listOf(rowSection1.pledger.toString()), pawnGoodsIdList)

    val dateForm310R1 = DataForm310R1(rowSection1.pawnPactId.toString(), subSectionR1123)

    addSection1(dateForm310R1)
}

private fun Data310Form.addClosedPawnPact(rowSection1: RowSection1) {

    val dateForm310R1 = DataForm310R1(rowSection1.pawnPactId.toString(), rowSection1.pawnpactClose)

    addSection1(dateForm310R1)
}

private const val SELECT_R1 = "{? = call od.PTKB_FORM_310.getSection1( ? ) }"

private const val SELECT_R1_3 = "{? = call od.PTKB_FORM_310.getSection1_3PawnGoods( ?, ? ) }"

private data class RowSection1(val pawnPactId: Number, val loanId: Number, val pawnpactOpen: Date,
                               val pawnpactLabel: String?, val signLoan: Date, val pawnpactClose: Date?,
                               val pledger: Number, val loanClient: Number, val isClosedPawnPact: Boolean)

private fun toRowSection1(row: Array<Any?>): RowSection1 {

    return RowSection1(pawnPactId = row[0] as Number,
        loanId = row[1] as Number,
        pawnpactOpen = row[2] as Date,
        pawnpactLabel = row[3] as? String,
        signLoan = row[4] as Date,
        pawnpactClose = row[6] as Date,
        pledger = row[7] as Number,
        loanClient = row[8] as Number,
        isClosedPawnPact = (row[9] as Number).toInt() != 0
    )
}