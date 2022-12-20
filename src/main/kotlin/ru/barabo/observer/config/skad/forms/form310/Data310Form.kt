package ru.barabo.observer.config.skad.forms.form310

import ru.barabo.observer.config.task.form310.section.r1.DataForm310R1
import ru.barabo.observer.config.task.form310.section.r2.DataForm310R2
import ru.barabo.observer.config.task.form310.section.r3.DataForm310R3
import ru.barabo.observer.config.task.form310.section.r4.DataForm310R4
import ru.barabo.observer.config.task.form310.section.r5.DataForm310R5
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R61
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R62
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R63
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R64

class Data310Form {

    lateinit var sectionsR1: List<DataForm310R1>

    lateinit var sectionsR2: List<DataForm310R2>

    lateinit var sectionsR3: List<DataForm310R3>

    lateinit var sectionsR4: List<DataForm310R4>

    lateinit var sectionsR5: List<DataForm310R5>

    lateinit var sectionsR61: List<DataForm310R61>

    lateinit var sectionsR62: List<DataForm310R62>

    lateinit var sectionsR63: List<DataForm310R63>

    lateinit var sectionsR64: List<DataForm310R64>

    internal fun addSection1(item: DataForm310R1) {
        if(!(::sectionsR1.isInitialized)) {
            sectionsR1 = ArrayList<DataForm310R1>()
        }

        (sectionsR1 as MutableList<DataForm310R1>).add(item)
    }

    internal fun addSection2(item: DataForm310R2) {
        if(!(::sectionsR2.isInitialized)) {
            sectionsR2 = ArrayList<DataForm310R2>()
        }

        (sectionsR2 as MutableList<DataForm310R2>).add(item)
    }

    internal fun addSection3(item: DataForm310R3) {
        if(!(::sectionsR3.isInitialized)) {
            sectionsR3 = ArrayList()
        }

        (sectionsR3 as MutableList<DataForm310R3>).add(item)
    }

    internal fun addSection4(item: DataForm310R4) {
        if(!(::sectionsR4.isInitialized)) {
            sectionsR4 = ArrayList()
        }

        (sectionsR4 as MutableList<DataForm310R4>).add(item)
    }

    internal fun addSection5(item: DataForm310R5) {
        if(!(::sectionsR5.isInitialized)) {
            sectionsR5 = ArrayList()
        }

        (sectionsR5 as MutableList<DataForm310R5>).add(item)
    }

    internal fun addSection62(item: DataForm310R62) {
        if(!(::sectionsR62.isInitialized)) {
            sectionsR62 = ArrayList()
        }

        (sectionsR62 as MutableList<DataForm310R62>).add(item)
    }

    internal fun addSection63(item: DataForm310R63) {
        if(!(::sectionsR63.isInitialized)) {
            sectionsR63 = ArrayList()
        }

        (sectionsR63 as MutableList<DataForm310R63>).add(item)
    }

    internal fun addSection64(item: DataForm310R64) {
        if(!(::sectionsR64.isInitialized)) {
            sectionsR64 = ArrayList()
        }

        (sectionsR64 as MutableList<DataForm310R64>).add(item)
    }
}


