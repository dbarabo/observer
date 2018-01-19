package ru.barabo.observer.config.cbr.ticket.task

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.OutType
import ru.barabo.observer.config.cbr.ticket.TicketPtkPsd
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtFromFns
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreDerby
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket440pFns : TicketLoader<KwtFromFns>(), FileFinder {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( ::getFolder440p, "KWTFCB.*\\.xml"))

    override val accessibleData: AccessibleData =
            AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override fun config(): ConfigTask = TicketPtkPsd

    override fun name(): String = "440-П Квитки из ИФНС"

    private fun getFolder440p() :File = File(Get440pFiles.getFolder440p())


    /**
     * добавляет в задачи - файл с ошибкой - чтобы его потом отправить по одному
     * клику
     */
    override fun processFailInfo(info : TicketInfo, file :File) {

        super.processFailInfo(info, file)

        val sessionSetting = AfinaQuery.uniqueSession()

        try {
            val (responseId, typeResponse) = parseResponse(file, sessionSetting)

            val repeatCreator = OutType.creatorByDbValue(typeResponse)
                    ?: throw Exception("Не найден creator с типом $typeResponse")

            val newElem = Elem(responseId, file.nameWithoutExtension, repeatCreator, Duration.ofHours(7))

            StoreDerby.save(newElem)

            AfinaQuery.commitFree(sessionSetting)
        } catch (e: Exception) {
            AfinaQuery.rollbackFree(sessionSetting)
        }
    }

    private val EXEC_REPEAT_RESPONSE = "{ call od.PTKB_440P.addFileByFailTicket(?, ?, ?) }"

    private fun parseResponse(fileTicket: File, sessionSetting: SessionSetting): Pair<Number, Int> {

        val outValues = AfinaQuery.execute(query = EXEC_REPEAT_RESPONSE,
                params = arrayOf(fileTicket.nameWithoutExtension),
                sessionSetting = sessionSetting,
                outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.NUMBER))

        return Pair(outValues!![0] as Number, (outValues[1] as Number).toInt())
    }
}