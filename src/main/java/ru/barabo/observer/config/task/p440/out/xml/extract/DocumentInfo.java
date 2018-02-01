package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("�������")
public class DocumentInfo {

	@XStreamAlias("������")
	private String vid;

	@XStreamAlias("������")
	private String number;

	@XStreamAlias("�������")
	private String date;

	public DocumentInfo(String vid, String number, Date date) {

		this.vid = vid;

		this.number = number;

		this.date = XmlLoader.formatDate(date);
	}
}
