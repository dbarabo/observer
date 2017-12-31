package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Наименование")
public class NameInfoSluga {

	@XStreamAlias("Выборы")
	@XStreamAsAttribute
	private String vibory; // Выборы
	
	@XStreamAlias("Субъект")
	@XStreamAsAttribute
	private String subject; // Субъект
	
	@XStreamAlias("ДатаСвед")
	@XStreamAsAttribute
	private String date;// ДатаСвед

	public String getVibory() {
		return vibory;
	}

	public void setVibory(String vibory) {
		this.vibory = vibory;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
