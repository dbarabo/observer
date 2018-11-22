package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("КЭСП")
public final class Kesp {

	@XStreamAlias("ИдКЭСП")
	private String idKesp;

	@XStreamAlias("ВалКЭСП")
	private String currency;

	public String getIdKesp() {
		return idKesp;
	}

	public String getCurrency() {
		return currency;
	}
}
