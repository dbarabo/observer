package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Р4.15")
public class SubSectionR415 {

    @XStreamAlias("Р4.15_2")
    final private String idCodeBill;

    @XStreamAlias("Р4.15_3")
    final private String typeBill;

    @XStreamAlias("Р4.15_4")
    final private String signBill;

    @XStreamAlias("Р4.15_5")
    final private String idSubjectCode;

    @XStreamAlias("Р4.15_6")
    final private String codeLocation;

    public SubSectionR415(String idCodeBill, String typeBill, String signBill, String idSubjectCode, String codeLocation) {

        this.idCodeBill = idCodeBill;

        this.typeBill = typeBill;

        this.signBill = signBill;

        this.idSubjectCode = idSubjectCode;

        this.codeLocation = codeLocation;
    }
}
