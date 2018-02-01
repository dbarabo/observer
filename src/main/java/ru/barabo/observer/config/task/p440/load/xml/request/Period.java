package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("��������")
public final class Period {


	@XStreamAlias("�������")
	private String fromDate;

	@XStreamAlias("�������")
	private String toDate;

	public java.sql.Date getFromDate() {
		return XmlLoader.parseDate(fromDate);
	}

	public java.sql.Date getToDate() {
		return XmlLoader.parseDate(toDate);
	}
}
