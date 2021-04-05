package ru.barabo.observer.config.skad.plastic.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object CheckTableSpace : SinglePerpetual {

    override fun name(): String = "Проверка Table space"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit = ChronoUnit.HOURS

    override val countTimes: Long = 12

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
        workTimeFrom = LocalTime.of(6, 30), workTimeTo = LocalTime.of(23, 50) )

    override fun execute(elem: Elem): State {

        val spaces = AfinaQuery.selectCursor(CURSOR_TABLE_SPACE)

        for (space in spaces) {
            val name = space[0] as String

            val freeMb = (space[1] as Number).toInt()

            val totalMb = (space[2] as Number).toInt()

            val minimumMb = (space[3] as Number).toInt()

            logger.error("TABLE_SPACE $name FREE=$freeMb TOTAL=$totalMb MIN=$minimumMb")

            if(freeMb <= minimumMb) {
                sendAlarm(name, freeMb, totalMb)
            }
        }

        return super.execute(elem)
    }

    private fun sendAlarm(tableSpace: String, freeMb: Int, totalMb: Int) {

        val subject = "✖☢☠ Место в TableSpace заканчивается!!!"

        val body = "Внимание! \n В TableSpace $tableSpace осталось меньше $freeMb MB, всего tableSpace занимает $totalMb MB"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.TTS, cc = BaraboSmtp.AUTO,
            subject = subject, body = body, charsetSubject = "UTF-8")
    }
}

private const val CURSOR_TABLE_SPACE = "{ ? = call od.PTKB_PRECEPT.getTableSpaceSize }"

private val logger = LoggerFactory.getLogger(CheckTableSpace::class.java)