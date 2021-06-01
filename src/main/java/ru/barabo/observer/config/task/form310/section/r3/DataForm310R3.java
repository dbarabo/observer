package ru.barabo.observer.config.task.form310.section.r3;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р3")
public class DataForm310R3 {

    @XStreamAlias("Р3_1")
    final private String idCodeSubjectPledge;

    @XStreamAlias("Р3_2")
    final private String codeCountryOksm;

    @XStreamAlias("Р3_3")
    final private String codeFias;

    @XStreamAlias("Р3_4")
    final private String codeOkato;

    @XStreamAlias("Р3_5")
    final private String postIndex;

    @XStreamAlias("Р3_6")
    final private String street;

    @XStreamAlias("Р3_7")
    final private String houseNumber;

    @XStreamAlias("Р3_8")
    final private String housing;

    @XStreamAlias("Р3_9")
    final private String letter;

    @XStreamAlias("Р3_10")
    final private String apartmentNumber;

    @XStreamAlias("Р3_11")
    final private String fullAddress;

    public DataForm310R3(Number idCodeSubjectPledge, Number codeCountryOksm, String codeFias, String codeOkato,
                         String postIndex, String street, String houseNumber, String housing, String letter,
                         String apartmentNumber, String fullAddress) {

        this.idCodeSubjectPledge = idCodeSubjectPledge.toString();

        this.codeCountryOksm = codeCountryOksm.toString();

        this.codeFias = codeFias;

        this.codeOkato = codeOkato;

        this.postIndex = postIndex;

        this.street = street;

        this.houseNumber = houseNumber;

        this.housing = housing;

        this.letter = letter;

        this.apartmentNumber = apartmentNumber;

        this.fullAddress = fullAddress;
    }
}
