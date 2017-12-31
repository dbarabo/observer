package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Адрес")
public class Adress {

	@XStreamAlias("КодСубъекта")
	@XStreamAsAttribute
	private String codeSubj; // КодСубъекта="21" 
	
	@XStreamAlias("НеконфАдрес")
	@XStreamAsAttribute
	private String neConfAdress; //НеконфАдрес="Чувашская Республика - Чувашия, г.Чебоксары"
	
	@XStreamAlias("КонфАдрес")
	@XStreamAsAttribute
	private String confAdress; // КонфАдрес="ул.Фруктовая, д.4, кв.15" 
	
	@XStreamAlias("МестоРождения")
	@XStreamAsAttribute
	private String birthPlace; //МестоРождения
	
	@Override
	public String toString() {
		
		return codeSubj + " " + neConfAdress + " " + confAdress;
	}
	
	public String getCodeSubj() {
		return codeSubj;
	}

	public String getNeConfAdress() {
		return neConfAdress;
	}

	public String getConfAdress() {
		return confAdress;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

}
