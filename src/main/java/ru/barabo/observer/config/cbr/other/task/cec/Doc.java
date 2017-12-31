package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Документ")
public class Doc {

	@XStreamAlias("КодВидДок")
	@XStreamAsAttribute
	private String kodVidDoc; //КодВидДок="21" 
	
	@XStreamAlias("Серия")
	@XStreamAsAttribute
	private String seria; //Серия="97 05"
	
	@XStreamAlias("Номер")
	@XStreamAsAttribute
	private String number; //Номер="395246
	
	public String getKodVidDoc() {
		return kodVidDoc;
	}


	public String getSeria() {
		return seria;
	}


	public String getNumber() {
		return number;
	}


	@Override
	public String toString() {
		return kodVidDoc + " " + seria + " " + number;
	}
}
