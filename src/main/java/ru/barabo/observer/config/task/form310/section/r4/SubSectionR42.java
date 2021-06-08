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

    @XStreamAlias("Р4.2_8")
    final private String shareSizePercent;

    @XStreamAlias("Р4.2_9")
    final private String areaSqM;

    @XStreamAlias("Р4.2_10")
    final private String codeLandCategory;

    @XStreamAlias("Р4.2_11")
    final private String permittedUseLandPlot;

    @XStreamAlias("Р4.2_12")
    final private String codePledgerRight;

    @XStreamAlias("Р4.2_13")
    final private String expiryDateLease;

    @XStreamAlias("Р4.2_14")
    final private String typeConstruction;

    public SubSectionR42(String propertyType, String cadastralNumber, String conditionalNumber, String purpose,
                         String name, String functionalGroup, Number shareSizePercent, Number areaSqM,
                         String codeLandCategory, String permittedUseLandPlot, String codePledgerRight,
                         Date expiryDateLease, String typeConstruction) {

        this.propertyType = propertyType;

        this.cadastralNumber = cadastralNumber;

        this.conditionalNumber = conditionalNumber;

        this.purpose = purpose;

        this.name = name;

        this.functionalGroup = functionalGroup;

        this.shareSizePercent = XmlLoader.formatDecimal3(shareSizePercent);

        this.areaSqM = areaSqM == null ? null : areaSqM.intValue() + "";

        this.codeLandCategory = codeLandCategory;

        this.permittedUseLandPlot = permittedUseLandPlot;

        this.codePledgerRight = codePledgerRight;

        this.expiryDateLease = XmlLoader.formatDate(expiryDateLease);

        this.typeConstruction = typeConstruction;
    }
}
