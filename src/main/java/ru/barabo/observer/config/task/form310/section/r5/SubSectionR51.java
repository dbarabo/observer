package ru.barabo.observer.config.task.form310.section.r5;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Р5.1")
public class SubSectionR51 {

    @XStreamAlias("Р5.1_2")
    private final String codeCountryOksm;

    @XStreamAlias("Р5.1_3")
    private final String fullName;

    @XStreamAlias("Р5.1_4")
    private final String shortName;

    @XStreamAlias("Р5.1_5")
    private final String ogrn;

    @XStreamAlias("Р5.1_6")
    private final String inn;

    @XStreamAlias("Р5.1_7")
    private final String kio;

    @XStreamAlias("Р5.1_8")
    private final String tin;

    @XStreamAlias("Р5.1_9")
    private final String lei;

    @XStreamImplicit(itemFieldName = "Р5.1_10")
    final private List<String> registerNumberCountry;

    public SubSectionR51(Number codeCountryOksm, String fullName, String shortName, String ogrn, String inn, String kio,
                         String tin, String lei, List<String> registerNumberCountry) {

        this.codeCountryOksm = codeCountryOksm.toString();

        this.fullName = fullName;

        this.shortName = shortName;

        this.ogrn = ogrn;

        this.inn = inn;

        this.kio = kio;

        this.tin = tin;

        this.lei = lei;

        this.registerNumberCountry = registerNumberCountry == null ? null : registerNumberCountry;
    }
}
