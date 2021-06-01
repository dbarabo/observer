package ru.barabo.observer.config.task.form310.section.r6;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р6.4")
public class DataForm310R64 {
    @XStreamAlias("Р6.4_1")
    final private String priorIdCodePledgeItem;

    @XStreamAlias("Р6.4_2")
    final private String validIdCodePledgeItem;

    public DataForm310R64(Number priorIdCodePledgeItem, Number validIdCodePledgeItem) {

        this.priorIdCodePledgeItem = priorIdCodePledgeItem.toString();

        this.validIdCodePledgeItem = validIdCodePledgeItem.toString();
    }
}
