package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р1.3")
public class SubSectionR13 {

    @XStreamAlias("Р1.3_2")
    final private String idObjectPledge;

    public SubSectionR13(String idObjectPledge) {

        this.idObjectPledge = idObjectPledge;
    }
}
