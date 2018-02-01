package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.ticket.KwtTicket;
import ru.barabo.observer.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("КВТНОПРИНТ")
public class KwtTicketInfo implements KwtTicket {

	@XStreamAlias("ИмяФайла")
	private String fileNameResponse;

	@XStreamAlias("ДатаВремяПроверки")
	private String dateTimeTicket;

	@XStreamImplicit(itemFieldName = "Результат")
	private List<KwtResultCode> results;

	@Override
	public String getResposeFileName() {
		return fileNameResponse;
	}

	static private String XML_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	@Override
	public Date getDateTimeTicket() {
		return DateUtils.getTimestamp(DateUtils.parseDateNotNull(
				dateTimeTicket, XML_DATE_TIME_FORMAT));
	}

	private List<KwtResultCode> getResultList() {
		if (results == null) {

			results = new ArrayList<KwtResultCode>();
			results.add(new KwtResultCode("01"));
		}

		return results;
	}

	@Override
	public List<String> getCodes() {

		return getResultList().stream().map(r -> r.getCodeResult()).collect(Collectors.toList());
	}

	@Override
	public List<String> getErrorDescriptions() {

		return getResultList().stream().map(r -> r.getErrorDescription())
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getErrorCodeAttributes() {

		return getResultList().stream().map(r -> r.getErrorAttribute())
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getErrorValueAttributes() {

		return getResultList().stream().map(r -> r.getErrorValue()).collect(Collectors.toList());
	}

}
