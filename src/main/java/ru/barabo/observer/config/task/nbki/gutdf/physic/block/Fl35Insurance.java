package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 35. Сведения о страховании предмета залога
 */
public class Fl35Insurance {

    @XStreamAlias("exist_0")
    private final StringElement exist0; // 35.1. Признак наличия страхования = 0

    @XStreamAlias("exist_1")
    private final StringElement exist1; // 35.1. Признак наличия страхования = 1

    @XStreamAlias("franchiseExist_0")
    private final StringElement franchiseExist0; // 35.4. Признак наличия франшизы = 0

    @XStreamAlias("franchiseExist_1")
    private final StringElement franchiseExist1; // 35.4. Признак наличия франшизы = 1

    @XStreamAlias("startDate")
    private final StringElement startDate; // emptyValueType 35.5. Дата начала действия страхования

    @XStreamAlias("insuranceEndDate")
    private final StringElement insuranceEndDate; // emptyValueType 35.6. Дата окончания действия страхования согласно договору

    @XStreamAlias("insuranceFactEndDate")
    private final StringElement insuranceFactEndDate; // minOccurs="0" 35.7. Дата фактического прекращения страхования

    @XStreamAlias("endCode")
    private final StringElement endCode; // minOccurs="0" 35.8. Код причины прекращения страхования

    public Fl35Insurance() {
        this.exist0 = new StringElement("");
        this.exist1 = null;

        this.franchiseExist0 = null;
        this.franchiseExist1 = null;
        this.startDate = null;
        this.insuranceEndDate = null;
        this.insuranceFactEndDate = null;
        this.endCode = null;
    }

    public Fl35Insurance(Date startDate, Date insuranceEndDate, Date insuranceFactEndDate, Integer endCode) {
        this.exist0 = null;
        this.exist1 = new StringElement("");

        this.franchiseExist0 = new StringElement("");
        this.franchiseExist1 = null;

        this.startDate = new StringElement(startDate == null ? "-" : XmlLoader.formatDate(startDate));

        this.insuranceEndDate = new StringElement(insuranceEndDate == null ? "-" : XmlLoader.formatDate(insuranceEndDate));

        this.insuranceFactEndDate = insuranceFactEndDate == null ? null : new StringElement(XmlLoader.formatDate(insuranceEndDate));

        this.endCode = endCode == null ? null : new StringElement(endCode.toString());
    }
}
