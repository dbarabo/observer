package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ФИОД")
public class Fio {

	@XStreamAlias("Фамилия")
	@XStreamAsAttribute
	private String Fam; // Фамилия
	
	public String getFam() {
		return Fam;
	}

	public String getName() {
		return Name;
	}

	public String getSecond() {
		return Second;
	}

	@XStreamAlias("Имя")
	@XStreamAsAttribute
	private String Name; // Имя
	
	@XStreamAlias("Отчество")
	@XStreamAsAttribute
	private String Second; // Отчество
	
	@XStreamAlias("ДатаРожд")
	@XStreamAsAttribute
	private String bearch; // ДатаРожд
	
	@Override
	public String toString() {
		return Fam + " " + Name + " " + Second + " " + bearch;
	}
	
	public String getbirth() {
		return bearch;
	}
}
