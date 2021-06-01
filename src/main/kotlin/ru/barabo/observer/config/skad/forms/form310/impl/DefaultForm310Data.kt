package ru.barabo.observer.config.skad.forms.form310.impl

import ru.barabo.observer.config.skad.forms.form310.Data310Form
import ru.barabo.observer.config.skad.forms.form310.ExecutorForm
import ru.barabo.observer.config.skad.forms.form310.Form310Data
import java.util.*

class DefaultForm310Data(override val dateReport: Date) : Form310Data {

    private val _data = checkCreateData310Form(dateReport)

    override val leader: ExecutorForm = leaderPtkb

    override val chiefAccountant: ExecutorForm = chiefAccountantPtkb

    override val executor: ExecutorForm = executorPtkb

    override val data: Data310Form?
        get() = _data
}

private val leaderPtkb = ExecutorForm("Председатель Правления", "Сима Оксана Анатольевна")

private val chiefAccountantPtkb = ExecutorForm("Врио главного бухгалтера", "Паллас Светлана Александровна")

private val executorPtkb = ExecutorForm("Ведущий экономист", "Кудрявцева Людмила Федоровна", "226-98-31")