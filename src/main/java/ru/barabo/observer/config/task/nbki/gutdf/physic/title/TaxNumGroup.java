package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class TaxNumGroup {

    @XStreamAlias("taxCode")
    private final StringElement taxCode = new StringElement("1");

    @XStreamAlias("taxNum")
    private final StringElement taxNum;

    public TaxNumGroup(String taxNum) {
        this.taxNum = new StringElement(taxNum);
    }
}
