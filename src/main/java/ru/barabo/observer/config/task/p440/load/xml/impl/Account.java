package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;

@XStreamAlias("����")
public final class Account {

	final static transient protected Logger logger = Logger.getLogger(Account.class
			.getName());

	@XStreamAlias("�����")
	private String code;

	@XStreamAlias("�����")
	private String type;

	public String getCode() {
		return code;
	}
}
