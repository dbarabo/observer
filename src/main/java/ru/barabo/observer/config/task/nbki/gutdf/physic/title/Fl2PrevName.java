package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("FL_2_PrevName")
public class Fl2PrevName {

    @XStreamAlias("prevNameFlag_0")
    private final StringElement prevNameFlag0;

    @XStreamAlias("prevNameFlag_1")
    private final StringElement prevNameFlag1;

    @XStreamAlias("lastName")
    private final StringElement lastName;

    @XStreamAlias("firstName")
    private final StringElement firstName;

    @XStreamAlias("middleName")
    private final StringElement middleName;

    @XStreamAlias("date")  // minOccurs="0"
    private final StringElement date;

    public Fl2PrevName() {
        prevNameFlag0 = new StringElement("");

        prevNameFlag1 = null;

        lastName = null;

        firstName = null;

        middleName = null;

        date = null;
    }

    public Fl2PrevName(String lastName, String firstName, String middleName) {
        prevNameFlag0 = null;

        prevNameFlag1 = new StringElement("");

        this.lastName = new StringElement(lastName);

        this.firstName = new StringElement(firstName);

        this.middleName = middleName == null ? null : new StringElement(middleName);

        date = null;
    }

    public StringElement getLastName() {
        return lastName;
    }

    public StringElement getFirstName() {
        return firstName;
    }

    public StringElement getMiddleName() {
        return middleName;
    }
}
