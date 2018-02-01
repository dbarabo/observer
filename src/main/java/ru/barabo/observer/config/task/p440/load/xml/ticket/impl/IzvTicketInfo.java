package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ticket.IzvTicket;
import ru.barabo.observer.utils.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@XStreamAlias("����������")
public class IzvTicketInfo implements IzvTicket {

	@XStreamAlias("��������")
	private String fileNameResponse;
	
	@XStreamAlias("��������������")
	private String codeResultCheck;
	
	@XStreamAlias("�����������������")
	private String dateTimeTicket;

	@XStreamAlias("���������")
	private String errorDescription;

	@XStreamAlias("����������������")
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
