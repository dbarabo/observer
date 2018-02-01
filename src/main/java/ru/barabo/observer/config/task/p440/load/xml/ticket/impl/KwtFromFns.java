package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.task.p440.load.xml.ticket.AbstractTicket;
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo;

@XStreamAlias("‘‡ÈÎ")
public class KwtFromFns extends AbstractTicket {

	final static transient private Logger logger = Logger.getLogger(KwtFromFns.class.getName());

	@XStreamAlias(" ¬“ÕŒœ–»Õ“")
	private KwtTicketInfo kwtTicket;

	@Override
	public TicketInfo getTicketInfo() {

		return kwtTicket;
	}
}
