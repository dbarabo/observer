package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.16")
public class SubSectionR416 {

    @XStreamAlias("Р4.16_2")
    final private String typeIssueGradeSecurity;

    @XStreamAlias("Р4.16_3")
    final private String count;

    @XStreamAlias("Р4.16_4")
    final private String codeIsin;

    @XStreamAlias("Р4.16_5")
    final private String stateRegistrationNumber;

    @XStreamAlias("Р4.16_6")
    final private String idNumber;

    public SubSectionR416(String typeIssueGradeSecurity, Number count, String codeIsin, String stateRegistrationNumber, String idNumber) {

        this.typeIssueGradeSecurity = typeIssueGradeSecurity;

        this.count = count.toString();

        this.codeIsin = codeIsin;

        this.stateRegistrationNumber = stateRegistrationNumber;

        this.idNumber = idNumber;
    }
}
