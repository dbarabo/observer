package ru.barabo.observer.config.cbr.ticket.task

import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.ptkpsd.PtkPsd
import ru.barabo.observer.config.cbr.ptkpsd.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.task.p440.TicketLoader
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.finder.FileFinder
import ru.barabo.observer.config.task.finder.FileFinderData
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtFromFns
import java.io.File
import java.time.Duration
import java.time.LocalTime

object Ticket440pFns : TicketLoader<KwtFromFns>(), FileFinder {

    override val fileFinderData: List<FileFinderData> =
            listOf(FileFinderData( ::getFolder440p, "KWTFCB.*\\.xml"))

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS, false, LocalTime.MIN, LocalTime.MAX, Duration.ofSeconds(1))

    override fun config(): ConfigTask = PtkPsd

    override fun name(): String = "440-П Квитки из ИФНС"

    private fun getFolder440p() :File = File(Get440pFiles.getFolder440p())


    override fun processFailInfo(info : TicketInfo, file :File) {

        super.processFailInfo(info, file)

        // TODO

        /**
         * добавляет в задачи - файл с ошибкой - чтобы его потом отправить по одному
         * клику
         */
//        private fun addFailFile(fileTicket: File) {
//
//            val outValues = getResponseId(fileTicket)
//
//            val responseId = (if (outValues!!.size == 0) null else outValues!!.get(0)) ?: return
//
//            val typeResponse = (outValues!!.get(1) as Number).toInt()
//
//            val pbType = PbType.getPbTypeByDbValue(typeResponse)
//            val actionType = if (pbType == null) null else pbType!!.getActionType()
//
//            var item = ViewerActionItem(null, responseId, actionType, false,
//                    Cfg.query440p().waitTimeMinuteResolveFailResponse())
//
//            item = FactoryDB.getViewerActionDB().addItem(item)
//
//            item.setTarget(fileTicket.name, true)
//        }
//
//        private fun getResponseId(fileTicket: File): List<Any>? {
//
//            val outValues = ArrayList<Any>()
//
//            val error = Query.executeOut(Cfg.query440p().execAddResponseByFailTicket(),
//                    arrayOf<Any>(FileUtil.removeExt(fileTicket.name)),
//                    intArrayOf(OracleTypes.NUMBER, OracleTypes.NUMBER), outValues,
//                    TransactType.COMMIT)
//
//            if (error != null) {
//                logger.error(error)
//                return null
//            }
//
//            return outValues
//        }


    }

}