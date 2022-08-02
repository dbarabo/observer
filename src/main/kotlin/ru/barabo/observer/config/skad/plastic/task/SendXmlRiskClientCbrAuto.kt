package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.forms.clientrisk.impl.DefaultClientRisk
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object SendXmlRiskClientCbrAuto : SinglePerpetual {

    override fun name(): String = "АвтоОтправка Клиентов с Рисками ЦБ"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit =  ChronoUnit.DAYS

    override val countTimes: Long = 1

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY, false,
        LocalTime.of(16, 29), LocalTime.of(23, 50), Duration.ofMinutes(1))

    override fun execute(elem: Elem): State {

        try {
            val fileForm = DefaultClientRisk(null).createFile( pathFolderRiskCbr() )

            val zipFileName = "${pathFolderRiskCbr()}/${fileForm.nameWithoutExtension}.zip"

            Archive.packToZip(zipFileName, fileForm)

        } catch (e: Exception) {
            logger.error("execute", e)

            return sendError(e.message!!)
        }

        return super.execute(elem)
    }
}

private fun sendError(error: String): State {
    BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO,
        subject = "Ошибка при ежедневной автовыгрузке xml Риски по клиентам для ЦБ",
        body = error
    )

    return State.ERROR
}

private val logger = LoggerFactory.getLogger(SendXmlRiskClientCbrAuto::class.java)

private fun pathFolderRiskCbr() = "$pathFolderRisk/${todayPath()}".byFolderExists().absolutePath

private fun todayPath(): String = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now())

private val pathFolderRisk = "H:/Gu_cb/Уровень риска из ЦБ/out".ifTest("C:/Gu_cb/Уровень риска из ЦБ/out")
