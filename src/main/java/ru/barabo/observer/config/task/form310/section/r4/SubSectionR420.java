package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Р4.20")
public class SubSectionR420 {

    @XStreamAlias("Р4.20_2")
    final private String typePropertyRights;

    @XStreamAlias("Р4.20_3")
    final private String name;

    @XStreamAlias("Р4.20_4")
    final private String typePropertyPact;

    @XStreamAlias("Р4.20_5")
    final private String datePropertyPact;

    @XStreamAlias("Р4.20_6")
    final private String numberPropertyPact;

    public SubSectionR420(String typePropertyRights, String name,
                          String typePropertyPact, Date datePropertyPact, String numberPropertyPact) {

        this.typePropertyRights = typePropertyRights;

        this.name = name;

        this.typePropertyPact = typePropertyPact;
        this.datePropertyPact = XmlLoader.formatDate(datePropertyPact);
        this.numberPropertyPact = numberPropertyPact;
    }
}
