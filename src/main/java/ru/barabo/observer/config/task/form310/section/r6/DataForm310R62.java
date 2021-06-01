package ru.barabo.observer.config.task.form310.section.r6;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р6.2")
public class DataForm310R62 {

    @XStreamAlias("Р6.2_1")
    final private String priorIdCodePactPledge;

    @XStreamAlias("Р6.2_2")
    final private String validIdCodePactPledge;

    public DataForm310R62(Number priorIdCodePactPledge, Number validIdCodePactPledge) {

        this.priorIdCodePactPledge = priorIdCodePactPledge.toString();

        this.validIdCodePactPledge = validIdCodePactPledge.toString();
    }
}
