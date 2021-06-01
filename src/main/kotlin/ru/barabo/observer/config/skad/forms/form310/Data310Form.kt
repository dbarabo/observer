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
            sectionsR3 = ArrayList<DataForm310R3>()
        }

        (sectionsR3 as MutableList<DataForm310R3>).add(item)
    }
}


