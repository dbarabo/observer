package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р1.5")
public class SectionR15 {

    @XStreamAlias("Р1.5_1")
    final private String idPactLoan;

    @XStreamAlias("Р1.5_2")
    final private String idPactPawn;

    public SectionR15(String idPactLoan, String idPactPawn) {

        this.idPactLoan = idPactLoan;
        this.idPactPawn = idPactPawn;
    }
}
