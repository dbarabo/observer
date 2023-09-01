package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФИО")
public class FioAttr {
    @XStreamAlias("Фамилия")
    private String lastName;

    @XStreamAlias("Имя")
    private String firstName;

    @XStreamAlias("Отчество")
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

    public FioAttr() {
    }

    public FioAttr(String firstName, String lastName, String secondName) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.papaName = secondName;
    }
}
