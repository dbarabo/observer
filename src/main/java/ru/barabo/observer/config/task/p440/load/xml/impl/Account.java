package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;

@XStreamAlias("—чет")
public final class Account {

	final static transient protected Logger logger = Logger.getLogger(Account.class
			.getName());

	@XStreamAlias("Ќом—ч")
	private String code;

	@XStreamAlias("¬ид—ч")
	private String type;

	public String getCode() {
		return code;
	}
}
