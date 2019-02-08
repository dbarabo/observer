package ru.barabo.observer.config.cbr.ptkpsd.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.task.form101.CheckerRedSaldo
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CheckerRedSaldoNow : Executor, ActionTask {

    override fun actionTask(): ActionTask = this

    override fun name(): String = "Красное сальдо при закрытии"

    override val accessibleData: AccessibleData = AccessibleData(executeWait = Duration.ofMinutes(1))

    override fun config(): ConfigTask = PtkPsd

    override fun findAbstract(): Executor? = findBySelect()

    override fun execute(elem: Elem): State {

        if(!isCloseArchiveDay(elem.idElem)) {
            elem.executed = (elem.executed ?: LocalDateTime.now()).plusMinutes(1)
            elem.state = State.NONE

            return State.NONE
        }

        return if(CheckerRedSaldo.isCheckSaldo(Timestamp(elem.idElem!!)))State.ERROR else State.OK
    }

    private fun isCloseArchiveDay(timeArchive: Long?): Boolean {

        val archDay = getArchiveDay() ?: return true

        return (timeArchive != archDay.time)
    }

    private fun findBySelect(): Executor? {
        val archDay = getArchiveDay() ?: return null

        val elem = Elem(idElem = archDay.time, name = archDay.formatDate(), task = this,
                executed = LocalDateTime.now().plus(accessibleData.executeWait))

        return if(StoreSimple.addNotExistsByIdElem(elem, State.NONE)) this else null
    }

    private fun getArchiveDay(): Timestamp? {
        val row = AfinaQuery.select(SELECT_OPER_DAY)[0]

        val archDay = row[0] as Timestamp

        val operDay = row[1] as Timestamp

        if(archDay.time == operDay.time) return null

        return archDay
    }

    private const val SELECT_OPER_DAY: String = "select o.arcdate, o.operdatebeg from od.operdateset o where rownum = 1"
}

private fun Timestamp.formatDate() = this.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ISO_DATE)