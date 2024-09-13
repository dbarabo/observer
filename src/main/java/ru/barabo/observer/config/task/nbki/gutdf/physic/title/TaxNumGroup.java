package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("taxNum_group")
public class TaxNumGroup {

    @XStreamAlias("taxNum")
    private final StringElement taxNum;

    @XStreamAlias("taxCode")
    private final StringElement taxCode = new StringElement("1");

    public TaxNumGroup(String taxNum) {
        this.taxNum = new StringElement(taxNum);
    }
}
