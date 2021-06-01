package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Р4.18")
public class SubSectionR418 {

    @XStreamAlias("Р4.18_2")
    final private String countCertificates;

    @XStreamAlias("Р4.18_3")
    final private String codeIsin;

    @XStreamAlias("Р4.18_4")
    final private String registrationNumber;

    @XStreamAlias("Р4.18_5")
    final private String individualDesignation;

    @XStreamAlias("Р4.18_6")
    final private String expirationDate;

    public SubSectionR418(Number countCertificates, String codeIsin, String registrationNumber,
                          String individualDesignation, Date expirationDate) {

        this.countCertificates = countCertificates.toString();

        this.codeIsin = codeIsin;

        this.registrationNumber = registrationNumber;

        this.individualDesignation = individualDesignation;

        this.expirationDate = XmlLoader.formatDate(expirationDate);
    }
}
