package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("ЗаПериод")
public final class Period {


	@XStreamAlias("ДатаНач")
	private String fromDate;

	@XStreamAlias("ДатаКон")
	private String toDate;

	public java.sql.Date getFromDate() {
		return XmlLoader.parseDate(fromDate);
	}

	public java.sql.Date getToDate() {
		return XmlLoader.parseDate(toDate);
	}
}
