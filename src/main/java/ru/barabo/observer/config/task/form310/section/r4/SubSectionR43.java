package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Р4.3")
public class SubSectionR43 {

    @XStreamAlias("Р4.3_2")
    final private String category;

    @XStreamAlias("Р4.3_3")
    final private String vinCar;

    @XStreamAlias("Р4.3_4")
    final private String idSelfPropelledCar;

    @XStreamAlias("Р4.3_5")
    final private String yearIssue;

    @XStreamAlias("Р4.3_6")
    final private String brand;

    @XStreamAlias("Р4.3_7")
    final private String model;

    @XStreamAlias("Р4.3_8")
    final private String frameNumber;

    @XStreamAlias("Р4.3_9")
    final private String enginePowerHP;

    @XStreamAlias("Р4.3_10")
    final private String enginePowerkW;

    @XStreamAlias("Р4.3_11")
    final private String engineDisplacementSm3;


    public SubSectionR43(String category, String vinCar, String idSelfPropelledCar, Number yearIssue, String brand,
                         String model, String frameNumber, String enginePowerHP, String enginePowerkW, Number engineDisplacementSm3) {

        this.category = category;

        this.vinCar = vinCar;

        this.idSelfPropelledCar = idSelfPropelledCar;

        this.yearIssue = yearIssue == null ? null : yearIssue.toString();

        this.brand = brand;

        this.model = model;

        this.frameNumber = frameNumber;

        this.enginePowerHP = enginePowerHP;

        this.enginePowerkW = enginePowerkW;

        this.engineDisplacementSm3 = XmlLoader.formatDecimal1(engineDisplacementSm3);
    }
}
