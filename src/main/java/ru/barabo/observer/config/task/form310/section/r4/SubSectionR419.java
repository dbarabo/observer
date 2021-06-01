package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.19")
public class SubSectionR419 {

    @XStreamAlias("Р4.19_2")
    final private String typeSecurity;

    @XStreamAlias("Р4.19_3")
    final private String securitySeries;

    @XStreamAlias("Р4.19_4")
    final private String securityNumber;

    public SubSectionR419(String typeSecurity, String securitySeries, String securityNumber) {

        this.typeSecurity = typeSecurity;

        this.securitySeries = securitySeries;

        this.securityNumber = securityNumber;
    }
}
