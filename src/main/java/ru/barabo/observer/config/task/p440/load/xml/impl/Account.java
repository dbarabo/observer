package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("����")
public final class Account {

	@XStreamAlias("�����")
	private String code;

	@XStreamAlias("�����")
	private String type;

	public String getCode() {
		return code;
	}
}
