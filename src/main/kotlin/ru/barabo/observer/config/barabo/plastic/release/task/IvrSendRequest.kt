package ru.barabo.observer.config.barabo.plastic.release.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.turn.task.IbiSendToJzdo.hCardOutSentTodayFolder
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.db.SingleSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.LocalTime

object IvrSendRequest: SingleSelector {

    override val select: String = "select id, client_name from od.ptkb_ivr_register where state = 0"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, workTimeFrom = LocalTime.of(7, 0))

    override fun name(): String = "ПИН-код: Отправить запрос"

    override fun config(): ConfigTask = PlasticReleaseConfig

    override fun execute(elem: Elem): State {

        val row = AfinaQuery.select(SELECT_REQUEST, arrayOf(elem.idElem!!))[0]

        val dataFile = dataRequest(row[0] as String, row[1] as String, row[2] as String)

        val fileName = row[3] as String

        val fileRequest = File("${hCardOutSentTodayFolder().absolutePath}/$fileName")

        fileRequest.writeText(dataFile)

        val fileToIvr = File("$IVR_SEND_TO_PATH/$fileName")

        fileRequest.copyTo(fileToIvr, true)

        return State.OK
    }

    private val IVR_SEND_TO_PATH = "\\\\jzdo/c$/quasionline-1.0.0/files/request"


    private val SELECT_REQUEST = "select CARD_NUMBER, CARD_VALID_TO, PHONE, FILE_NAME from od.PTKB_IVR_REGISTER where id = ?"

    private fun dataRequest(cardNumber :String, cardEnd :String, phone :String) :String =
            "<allowPinSetting>\n" +
                    "\t<request>\n" +
                    "\t\t<callingSystemId>0226</callingSystemId>\n" +
                    "\t\t<cardId>$cardNumber=>$cardEnd</cardId>\n" +
                    "\t\t<cardIdType>P</cardIdType>\n" +
                    "\t\t<clientPhone>$phone</clientPhone>\n" +
                    "\t</request>\n" +
            "</allowPinSetting>"
}