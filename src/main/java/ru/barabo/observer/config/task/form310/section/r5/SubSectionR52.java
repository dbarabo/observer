package ru.barabo.observer.config.task.form310.section.r5;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р5.2")
public class SubSectionR52 {

    @XStreamAlias("Р5.2_2")
    private final String typePhysic;

    @XStreamAlias("Р5.2_3")
    private final String codeCountryOksm;

    @XStreamAlias("Р5.2_4")
    private final String surname;

    @XStreamAlias("Р5.2_5")
    private final String firstName;

    @XStreamAlias("Р5.2_6")
    private final String middleName;

    @XStreamAlias("Р5.2_7")
    private final String inn;

    @XStreamAlias("Р5.2_8")
    private final String ogrnIp;

    @XStreamAlias("Р5.2_9")
    private final String codeDocument;

    @XStreamAlias("Р5.2_10")
    private final String seriesDocument;

    @XStreamAlias("Р5.2_11")
    private final String numberDocument;

    public SubSectionR52(String typePhysic, Number codeCountryOksm, String surname, String firstName, String middleName,
                         String inn, String ogrnIp, String codeDocument, String seriesDocument, String numberDocument) {

        this.typePhysic = typePhysic;

        this.codeCountryOksm = codeCountryOksm.toString();

        this.surname = surname;

        this.firstName = firstName;

        this.middleName = middleName;

        this.inn = inn;

        this.ogrnIp = ogrnIp;

        this.codeDocument = codeDocument;

        this.seriesDocument = seriesDocument;

        this.numberDocument = numberDocument;
    }
}
