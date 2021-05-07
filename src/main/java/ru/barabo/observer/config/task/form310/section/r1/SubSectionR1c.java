package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р1с")
public class SubSectionR1c {

    @XStreamAlias("Р1.1_2")
    final private String idPactLoan;

    public SubSectionR1c(String idPactLoan) {

        this.idPactLoan = idPactLoan;
    }
}
