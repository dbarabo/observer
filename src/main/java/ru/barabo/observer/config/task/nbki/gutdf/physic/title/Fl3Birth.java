package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

@XStreamAlias("FL_3_Birth")
public class Fl3Birth {

    @XStreamAlias("birthDate")
    private final StringElement birthDate;

    @XStreamAlias("countryCode")
    private final StringElement countryCode = new StringElement("643");

    @XStreamAlias("birthPlace")
    private final StringElement birthPlace;

    public Fl3Birth(Date birthDate, String birthPlace) {

        this.birthDate = new StringElement( XmlLoader.formatDate(birthDate) );

        this.birthPlace = new StringElement(birthPlace);
    }
}
