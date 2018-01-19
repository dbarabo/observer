package ru.barabo.observer.config.cbr.ticket

import ru.barabo.observer.config.cbr.ticket.task.*
import ru.barabo.observer.config.task.ActionTask

enum class PrefixTicket(private val prefix :String,
                        private val text0Ticket :ActionTask?,
                        private val cab_Ticket :ActionTask?) {

    P311("2z", Ticket311pCbr, Ticket311pFns),
    P349_FSFM("oz", TicketFsfm349p, Fsfm349pRequest),
    P364("sz", Ticket364FtsText, Ticket364FtsCab),
    FTS("tz", TicketFtsText, TicketFtsCab),
    LEGAL("0z", TicketLegalText, TicketLegalCab),
    VAL_VBK("nz", null, TicketVbkArchive),
    P550("wz", GetProcess550pFiles, Ticket550p),
    P440 ("mz", null, Get440pFiles);

    companion object {

        private val HASH_PREFIX_TICKET = PrefixTicket.values().map {
            Pair(it.prefix, Pair(it.text0Ticket, it.cab_Ticket) ) }.toMap()

        fun ticketByPrefix(name: String, nullTask :ActionTask) :ActionTask {

            val isFirst = if(name.length >= 6) name.substring(5, 6) == "0"
            else throw Exception("mask file is not valid name= $name")

            return HASH_PREFIX_TICKET[name.substring(0, 2)]
                    ?.let { if(isFirst) it.first else it.second }?:nullTask
        }
    }


}