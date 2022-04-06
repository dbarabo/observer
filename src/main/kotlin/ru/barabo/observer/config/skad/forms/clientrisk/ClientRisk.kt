package ru.barabo.observer.config.skad.forms.clientrisk

import ru.barabo.observer.config.task.clientrisk.ClientForm
import java.io.File

interface ClientRisk {

    fun createFile(): File

    fun clientForms(): List<ClientForm>
}