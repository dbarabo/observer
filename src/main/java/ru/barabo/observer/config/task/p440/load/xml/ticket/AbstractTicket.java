package ru.barabo.observer.config.task.p440.load.xml.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class AbstractTicket {

	@XStreamAlias("���������")
	protected String fnsPost;

	@XStreamAlias("�������")
	protected String fnsPhone;

	@XStreamAlias("�������")
	protected String fnsFio;

	@XStreamAlias("������")
	protected String typeInfo;

	abstract public TicketInfo getTicketInfo();

}
