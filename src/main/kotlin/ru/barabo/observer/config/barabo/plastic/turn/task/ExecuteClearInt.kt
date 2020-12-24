package ru.barabo.observer.config.barabo.plastic.turn.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.turn.PlasticTurnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.mail.smtp.BaraboSmtp
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime

object ExecuteClearInt : SingleSelector {

    override val select: String =
        "select c.id, c.file_name from od.ptkb_clearint c where c.state = 0 and trunc(ondate) = trunc(sysdate)"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(12, 0),
        workTimeTo =  LocalTime.of(23, 45), executeWait = Duration.ofMinutes(1))

    override fun name(): String = "CLEARINT-создать проводки"

    override fun config(): ConfigTask = PlasticTurnConfig

    override fun execute(elem: Elem): State {

        AfinaQuery.execute(EXEC_CLEARINT, arrayOf(elem.idElem))

        sendMailCorrespondInfo(elem.name.substringBefore('.'))

        return State.OK
    }

    private fun sendMailCorrespondInfo(fileName: String) {

        val info = """Созданы проводки корсчета на основе файла $fileName
Завтра, после получения выписки от ПЦ, зайдите в архивную дату 21:20. 
Найдите общего папу с номером $fileName
Нажмите Shift+F9
Сверьте каждый внутренний перевод (для открытия перевода нажмите F7)
После сверки, если все сходится исполните общего папу (на папе правой кнопкой - исполнить)
"""

        val subject = "Созданы проводки по платежным системам $fileName"

        BaraboSmtp.sendStubThrows(to = BaraboSmtp.CORRESPOND, bcc = BaraboSmtp.AUTO,
            subject = subject, body = info, charsetSubject = "UTF-8")
    }
}

private const val EXEC_CLEARINT = "{ call od.PTKB_PLASTIC_TURN.processClearInt(?) }"