package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФИО")
public final class Fio {

    @XStreamAlias("Фамилия")
    private String lastName;

    @XStreamAlias("Имя")
    private String firstName;

    @XStreamAlias("Отчество")
    private String papaName;

    @XStreamAlias("ПрОтчество")
    private String absentPapaName;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPapaName() {

        return papaName == null ? null : papaName;
    }

    public String getAbsentPapaName() {

        return absentPapaName == null ? null : absentPapaName;
    }

    public Fio() {
    }

    public Fio(String firstName, String lastName, String secondName) {

        this.firstName = firstName;
        this.lastName = lastName;

        if(secondName == null || secondName.isEmpty()) {
            this.papaName = null;
            this.absentPapaName = "1";
        } else {
            this.absentPapaName = null;
            this.papaName = secondName;
        }
    }
}
