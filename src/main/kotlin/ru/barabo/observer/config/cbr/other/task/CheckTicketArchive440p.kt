package ru.barabo.observer.config.cbr.other.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.other.OtherCbr
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.LocalTime

object CheckTicketArchive440p: SingleSelector {

    override val select: String =
            "select id, FILE_NAME from od.ptkb_440p_archive where state != 99 and sysdate - created > 2/24"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(13, 0),
            workTimeTo = LocalTime.of(18, 0))

    override fun name(): String = "Нет квитков на Архив"

    override fun config(): ConfigTask = OtherCbr

    override fun execute(elem: Elem) : State {

        val data = AfinaQuery.select(SELECT_ARCHIVE_INFO, arrayOf(elem.idElem)).first()

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.AUTO, subject = SUBJECT_440P_ERROR,
                body = errorMessage(elem.name, data[0], data[1]) )

        return State.OK
    }

    private val SELECT_ARCHIVE_INFO = "select to_char(a.created, 'dd.mm.yy hh24:mi:ss'), " +
            "decode(a.STATE, 0, 'СОЗДАН', 1, 'Отправлен', 2, 'Подписан', 5, 'Отправлен', 99, " +
            "'Получена квитанция', to_char(a.STATE) ) from od.ptkb_440p_archive a where a.id = ?"

    private val SUBJECT_440P_ERROR = "440-П Ошибка"

    private fun errorMessage(fileName :String, created :Any?, state :Any?) = "На отправленный архив до сих пор не получена квитанция\n" +
            "\tФайл архива: $fileName\n" +
            "\tСоздан: $created\n" +
            "\tСостояние архива: $state"
}