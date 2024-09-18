package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf

import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl
import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import java.util.*

interface GutdfData {

    fun filenameWithoutExt(): String

    fun dateDocument(): Date

    fun subjectsCount(): Int

    fun groupBlocksCount(): Int

    fun subjectFlList(): List<SubjectFl>?

    fun subjectUlList(): List<SubjectUl>?
}