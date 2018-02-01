package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ticket.AbstractTicket;
import ru.barabo.observer.config.task.p440.load.xml.ticket.TicketInfo;

@XStreamAlias("����")
public class IzvFromFns extends AbstractTicket {

	@XStreamAlias("����������")
	private IzvTicketInfo izvTicket;

	@Override
	public TicketInfo getTicketInfo() {

		return izvTicket;
	}
}

