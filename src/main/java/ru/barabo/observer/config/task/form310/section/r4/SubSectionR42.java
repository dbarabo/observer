package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Р4.2")
public class SubSectionR42 {

    @XStreamAlias("Р4.2_2")
    final private String propertyType;

    @XStreamAlias("Р4.2_3")
    final private String cadastralNumber;

    @XStreamAlias("Р4.2_4")
    final private String conditionalNumber;

    @XStreamAlias("Р4.2_5")
    final private String purpose;

    @XStreamAlias("Р4.2_6")
    final private String name;

    @XStreamAlias("Р4.2_7")
    final private String functionalGroup;

    @XStreamAlias("Р4.2_8н")
    final private String numeratorShareInRight;

    @XStreamAlias("Р4.2_9н")
    final private String denominatorShareInRight;

    @XStreamAlias("Р4.2_10н")
    final private String areaSqM;

    @XStreamAlias("Р4.2_11н")
    final private String codeLandCategory;

    @XStreamAlias("Р4.2_12н")
    final private String permittedUseLandPlot;

    @XStreamAlias("Р4.2_13н")
    final private String codePledgerRight;

    @XStreamAlias("Р4.2_14н")
    final private String expiryDateLease;

    @XStreamAlias("Р4.2_15н")
    final private String typeConstruction;

    @XStreamAlias("Р4.2_16н")
    final private String areaSqMFree;

    @XStreamAlias("Р4.2_17н")
    final private String idFromForeign;

    @XStreamAlias("Р4.2_18")
    final private String rightRent;

    public SubSectionR42(String propertyType, String cadastralNumber, String conditionalNumber, String purpose,
                         String name, String functionalGroup,
                         Number numeratorShareInRight, Number denominatorShareInRight,
                         Number areaSqM, String codeLandCategory, String permittedUseLandPlot, String codePledgerRight,
                         Date expiryDateLease, String typeConstruction,
                         String areaSqMFree, String idFromForeign, String rightRent
                         ) {

        this.propertyType = propertyType;

        this.cadastralNumber = cadastralNumber;

        this.conditionalNumber = conditionalNumber;

        this.purpose = purpose;

        this.name = name;

        this.functionalGroup = functionalGroup;

        this.numeratorShareInRight = numeratorShareInRight == null ? null : numeratorShareInRight.intValue() + "";

        this.denominatorShareInRight = denominatorShareInRight == null ? null : denominatorShareInRight.intValue() + "";

        this.areaSqM = areaSqM == null ? null : areaSqM.intValue() + "";

        this.codeLandCategory = codeLandCategory;

        this.permittedUseLandPlot = permittedUseLandPlot;

        this.codePledgerRight = codePledgerRight;

        this.expiryDateLease = XmlLoader.formatDate(expiryDateLease);

        this.typeConstruction = typeConstruction;

        this.areaSqMFree = areaSqMFree;
        this.idFromForeign = idFromForeign;
        this.rightRent = rightRent;
    }
}
