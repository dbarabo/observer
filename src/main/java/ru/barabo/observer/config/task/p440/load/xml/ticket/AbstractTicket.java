package ru.barabo.observer.config.task.p440.load.xml.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class AbstractTicket {

	@XStreamAlias("ДолжнОтпр")
	protected String fnsPost;

	@XStreamAlias("ТелОтпр")
	protected String fnsPhone;

	@XStreamAlias("ФамОтпр")
	protected String fnsFio;

	@XStreamAlias("ТипИнф")
	protected String typeInfo;

	abstract public TicketInfo getTicketInfo();

}
