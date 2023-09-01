package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФИО")
public final class Fio {

	@XStreamAlias("Фамилия")
	private StringElement lastName;

	@XStreamAlias("Имя")
	private StringElement firstName;

	@XStreamAlias("Отчество")
	private StringElement papaName;

	public String getLastName() {
		return lastName.value;
	}

	public String getFirstName() {
		return firstName.value;
	}

	public String getPapaName() {
		return papaName.value;
	}

	public Fio() {
	}

	public Fio(String firstName, String lastName, String secondName) {

		this.firstName = new StringElement(firstName);
		this.lastName = new StringElement(lastName);
		this.papaName = new StringElement(secondName);
	}
}
