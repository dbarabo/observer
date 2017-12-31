package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Запрос")
public class Zapros {

	@XStreamAlias("Ид")
	@XStreamAsAttribute
	private String id; // Ид="ACA8119183AA4B22AC89525F2B4473EE"

	@XStreamAlias("Дата")
	@XStreamAsAttribute
	private String date;// Дата="01.07.2016"
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
