package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("����")
public final class Kesp {

	@XStreamAlias("������")
	private String idKesp;

	@XStreamAlias("�������")
	private String currency;

	public String getIdKesp() {
		return idKesp;
	}

	public String getCurrency() {
		return currency;
	}
}
