package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("РеквДок")
public class DocumentInfo {

	@XStreamAlias("ВидДок")
	private String vid;

	@XStreamAlias("НомДок")
	private String number;

	@XStreamAlias("ДатаДок")
	private String date;

	public DocumentInfo(String vid, String number, Date date) {

		this.vid = vid;

		this.number = number;

		this.date = XmlLoader.formatDate(date);
	}
}
