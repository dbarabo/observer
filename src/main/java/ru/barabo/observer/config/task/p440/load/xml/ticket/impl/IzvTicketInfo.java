package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ticket.IzvTicket;
import ru.barabo.observer.utils.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@XStreamAlias("ИЗВЦБКОНТР")
public class IzvTicketInfo implements IzvTicket {

	@XStreamAlias("ИмяФайла")
	private String fileNameResponse;
	
	@XStreamAlias("КодРезПроверки")
	private String codeResultCheck;
	
	@XStreamAlias("ДатаВремяПроверки")
	private String dateTimeTicket;

	@XStreamAlias("Пояснение")
	private String errorDescription;

	@XStreamAlias("ДатаВремяПериода")
	private String accessDateTime;

	@Override
	public String getResposeFileName() {
		return fileNameResponse;
	}

	@Override
	public List<String> getCodes() {
		return Collections.singletonList(codeResultCheck);
	}

	@Override
	public Date getDateTimeTicket() {

		return DateUtils.getTimestamp(DateUtils.parseDateNotNull(dateTimeTicket, XML_DATE_TIME_FORMAT));
	}

	@Override
	public List<String> getErrorDescriptions() {
		return Collections.singletonList(errorDescription);
	}

	@Override
	public Date getAccessDateTime() {

		return DateUtils.getTimestamp(DateUtils.parseDateNotNull(accessDateTime, XML_DATE_TIME_FORMAT));
	}

	private static String XML_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

}
