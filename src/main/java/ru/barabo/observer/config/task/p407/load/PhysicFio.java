package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФЛФИО")
public class PhysicFio {

    @XStreamAlias("Фам")
    private String family;

    @XStreamAlias("Имя")
    private String name;

    @XStreamAlias("Отч")
    private String secondName;

    public String getFullName() {
        return (family + " " + name + " " + secondName).trim();
    }

    public String getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }
}
