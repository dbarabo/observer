package ru.barabo.observer.config.barabo.p440.task

import oracle.jdbc.OracleTypes
import ru.barabo.db.SessionSetting
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.OutType
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtFromFns
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket440pFns : TicketLoader<KwtFromFns>(), FileFinder {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( Get440pFiles::getFolder440p, "KWTFCB.*\\.xml"))

    override val accessibleData: AccessibleData =
            AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    override fun config(): ConfigTask = ScadConfig // P440Config

    override fun name(): String = "440-П Квитки из ИФНС"

    /**
     * добавляет в задачи - файл с ошибкой - чтобы его потом отправить по одному
     * клику
     */
    override fun processFailInfo(info: TicketInfo, file: File) {

        super.processFailInfo(info, file)

        val sessionSetting = AfinaQuery.uniqueSession()

        try {
            val (responseId, typeResponse) = parseResponse(file, sessionSetting)

            responseId?.let {
                val repeatCreator = OutType.creatorByDbValue(typeResponse!!)
                        ?: throw Exception("Не найден creator с типом $typeResponse")

                val newElem = Elem(responseId, file.nameWithoutExtension, repeatCreator, Duration.ofHours(7))

                StoreSimple.save(newElem)
            }

            AfinaQuery.commitFree(sessionSetting)
        } catch (e: Exception) {
            AfinaQuery.rollbackFree(sessionSetting)
        }
    }

    private const val EXEC_REPEAT_RESPONSE = "{ call od.PTKB_440P.addFileByFailTicket(?, ?, ?) }"

    private fun parseResponse(fileTicket: File, sessionSetting: SessionSetting): Pair<Number?, Int?> {

        val outValues = AfinaQuery.execute(query = EXEC_REPEAT_RESPONSE,
                params = arrayOf(fileTicket.nameWithoutExtension),
                sessionSetting = sessionSetting,
                outParamTypes = intArrayOf(OracleTypes.NUMBER, OracleTypes.NUMBER))

        return Pair(outValues!![0] as? Number,(outValues[1] as? Number)?.toInt())
    }
}