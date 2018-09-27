package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("—чет")
public final class Account {

	@XStreamAlias("Ќом—ч")
	private String code;

	@XStreamAlias("¬ид—ч")
	private String type;

	public String getCode() {
		return code;
	}
}
