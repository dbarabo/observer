package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.task.form310.section.r1.SectionR15
import ru.barabo.observer.config.task.form310.section.r1.SectionR16
import java.util.*

internal fun Data310Form.addSection15(dateReport: Date) {
    val params = arrayOf<Any?>( java.sql.Date(dateReport.time))

    val section15Data = AfinaQuery.selectCursor(SELECT_R15, params)

    if(section15Data.isEmpty()) {
        this.sectionsR15 = emptyList()
        return
    }

    for(row in section15Data) {

        toNumberPairString(row).apply {
            addSection15(SectionR15(first, second) )
        }
    }
}

internal fun Data310Form.addSection16(dateReport: Date) {
    val params = arrayOf<Any?>( java.sql.Date(dateReport.time))

    val section16Data = AfinaQuery.selectCursor(SELECT_R16, params)

    if(section16Data.isEmpty()) {
        this.sectionsR16 = emptyList()
        return
    }

    for(row in section16Data) {

        toNumberPairString(row).apply {
            addSection16(SectionR16(first, second) )
        }
    }
}

private fun toNumberPairString(row: Array<Any?>): Pair<String, String> {

    return Pair((row[0] as Number).toLong().toString(), (row[1] as Number).toLong().toString())
}

private const val SELECT_R15 = "{ ? = call od.PTKB_FORM_310.getSection15( ? ) }"

private const val SELECT_R16 = "{ ? = call od.PTKB_FORM_310.getSection16( ? ) }"
