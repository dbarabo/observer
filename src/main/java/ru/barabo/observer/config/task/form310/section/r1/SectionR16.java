package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р1.6")
public class SectionR16 {
    @XStreamAlias("Р1.6_1")
    final private String idPactLoan;

    @XStreamAlias("Р1.6_2")
    final private String idSubject;

    public SectionR16(String idPactLoan, String idSubject) {

        this.idPactLoan = idPactLoan;
        this.idSubject = idSubject;
    }
}
