package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PersInfo")
public class PersInfo {

	@XStreamAlias("ФИОД")
	private Fio fio; // ФИОД
	
	@XStreamAlias("Документ")
	private Doc doc; // Документ
	
	@XStreamAlias("Адрес")
	private Adress adress; // Адрес
	
	
	public Fio getFio() {
		return fio;
	}

	public Doc getDoc() {
		return doc;
	}

	public Adress getAdress() {
		return adress;
	}
	
	@Override
	public String toString() {
		return "ФИО=" + fio.toString() +
			    " ДОК=" + doc.toString() + " Адрес=" + adress.toString();
	}
}
