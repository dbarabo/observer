package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Счет")
public final class Account {

	@XStreamAlias("НомСч")
	private String code;

	@XStreamAlias("ВидСч")
	private String type;

	public String getCode() {
		return code;
	}
}
