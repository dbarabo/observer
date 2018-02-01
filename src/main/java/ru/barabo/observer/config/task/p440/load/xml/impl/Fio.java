package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("���")
public final class Fio {

	@XStreamAlias("�������")
	private String lastName;

	@XStreamAlias("���")
	private String firstName;

	@XStreamAlias("��������")
	private String papaName;

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPapaName() {
		return papaName;
	}

	public Fio() {
	}

	public Fio(String firstName, String lastName, String secondName) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.papaName = secondName;
	}
}
