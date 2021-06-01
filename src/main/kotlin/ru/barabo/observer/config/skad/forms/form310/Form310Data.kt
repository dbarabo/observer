package ru.barabo.observer.config.skad.forms.form310

import java.util.*

interface Form310Data {

    val dateReport: Date

    val leader: ExecutorForm

    val chiefAccountant: ExecutorForm

    val executor: ExecutorForm

    val data: Data310Form?
}

data class ExecutorForm(val post: String, val name: String, val phone: String? = null)

