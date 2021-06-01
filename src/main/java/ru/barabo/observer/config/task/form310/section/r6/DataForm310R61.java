package ru.barabo.observer.config.task.form310.section.r6;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.form310.section.r5.SubSectionR51;

@XStreamAlias("Р6.1")
public class DataForm310R61 {

    @XStreamAlias("Р6.1_1")
    final private String priorIdCodePact;

    @XStreamAlias("Р6.1_2")
    final private String validIdCodePact;

    public DataForm310R61(Number priorIdCodePact, Number validIdCodePact) {

        this.priorIdCodePact = priorIdCodePact.toString();

        this.validIdCodePact = validIdCodePact.toString();
    }
}
