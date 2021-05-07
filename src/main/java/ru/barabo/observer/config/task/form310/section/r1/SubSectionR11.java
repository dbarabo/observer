package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;
import java.util.List;

@XStreamAlias("Р1.1")
public class SubSectionR11 {

    @XStreamAlias("Р1.1_3")
    final private String datePledgePactOpen;

    @XStreamAlias("Р1.1_4")
    final private String labelPledgePact;

    public SubSectionR11(Date pledgePactOpen, String labelPledgePact) {

        this.datePledgePactOpen = XmlLoader.formatDate( pledgePactOpen );

        this.labelPledgePact = labelPledgePact;
    }
}
