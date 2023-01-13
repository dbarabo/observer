package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R62
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R63
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R64
import java.sql.Date

internal fun Data310Form.addSection6(dateReport: java.util.Date) {

    val params = arrayOf<Any?>( Date(dateReport.time))

    this.sectionsR61 = emptyList()

    addSection6R2(params)
    addSection6R3(params)
    addSection6R4(params)
}

private fun Data310Form.addSection6R2(params: Array<Any?>) {
    val section = AfinaQuery.selectCursor(SELECT_R6_2, params)
    for(row in section) {

        toRowPairNumber(row).apply {
            addSection62( DataForm310R62(first, second) )
        }
    }
    if(section.isEmpty() ) {
        this.sectionsR62 = emptyList()
    }
}

private fun Data310Form.addSection6R3(params: Array<Any?>) {
    val section = AfinaQuery.selectCursor(SELECT_R6_3, params)
    for(row in section) {

        toRowPairNumber(row).apply {
            addSection63( DataForm310R63(first, second) )
        }
    }
    if(section.isEmpty() ) {
        this.sectionsR63 = emptyList()
    }
}

private fun Data310Form.addSection6R4(params: Array<Any?>) {
    val section = AfinaQuery.selectCursor(SELECT_R6_4, params)
    for(row in section) {

        toRowPairNumber(row).apply {
            addSection64( DataForm310R64(first, second) )
        }
    }
    if(section.isEmpty() ) {
        this.sectionsR64 = emptyList()
    }
}

private const val SELECT_R6_2 = "{ ? = call od.PTKB_FORM_310.getSection6_2( ? ) }"

private const val SELECT_R6_4 = "{ ? = call od.PTKB_FORM_310.getSection6_4( ? ) }"

private const val SELECT_R6_3 = "{ ? = call od.PTKB_FORM_310.getSection6_3( ? ) }"

private fun toRowPairNumber(params: Array<Any?>) = Pair(params[0] as Number, params[1] as Number)