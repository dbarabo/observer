package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("FL_1_Name")
public class Fl1Name {

    @XStreamAlias("lastName")
    private final StringElement lastName;

    @XStreamAlias("firstName")
    private final StringElement firstName;

    @XStreamAlias("middleName")
    private final StringElement middleName;

    public Fl1Name(String lastName, String firstName, String middleName) {

        this.lastName = new StringElement(lastName);

        this.firstName = new StringElement(firstName);

        this.middleName = middleName == null ? null : new StringElement(middleName);
    }
}
