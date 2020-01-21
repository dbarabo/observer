package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.exchange.VisaCalculator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.task.EXEC_TO_STATE
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalTime

object UpdaterCrossRateMtl : SingleSelector {

    override fun name(): String = "Пересчитать курсы Visa в MTL"

    override fun config(): ConfigTask = PlasticOutSide

    override val select: String = "select p.id, p.file_name from od.ptkb_ctl_mtl p where p.state = $WAIT_CROSS_RATE_STATUS"

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.ALL_DAYS,
            workTimeFrom = LocalTime.of(7, 0), workTimeTo = LocalTime.of(23, 50),
            executeWait = Duration.ZERO)

    override fun execute(elem: Elem): State {

        val fileId = elem.idElem

        val values = AfinaQuery.selectCursor(SELECT_CROSS_BORDER_VISA, arrayOf(fileId))

        for (row in values) {

            val kopeika = (row[1] as Number).toLong()

            val date = (row[2] as Timestamp).toLocalDateTime()

            val usdCent = try {
                VisaCalculator.convertRurToUsd(kopeika, date)
            } catch (e: Exception) {
                Long::class.javaObjectType
            }

            AfinaQuery.execute(EXEC_UPDATE_CROSS_TRANSACT, arrayOf(row[0], usdCent))
        }

        AfinaQuery.execute(EXEC_TO_STATE, arrayOf(NEW_STATUS, fileId))

        return State.OK
    }
}

private const val NEW_STATUS = 0

private const val WAIT_CROSS_RATE_STATUS = 3

private const val SELECT_CROSS_BORDER_VISA = "{ ? = call od.PTKB_PLASTIC_TURN.getCrossBorderVisa( ? ) }"

private const val EXEC_UPDATE_CROSS_TRANSACT = "{ call od.PTKB_PLASTIC_TURN.updateCrossBorderVisa(?, ?) }"