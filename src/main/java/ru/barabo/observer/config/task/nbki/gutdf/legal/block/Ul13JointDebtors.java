package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 13. Сведения о солидарных должниках
 */
public class Ul13JointDebtors {

    @XStreamAlias("exist_0")
    private final StringElement exist0; //20.1. Признак наличия солидарных должников = 0

    @XStreamAlias("exist_1")
    private final StringElement exist1; //20.1. Признак наличия солидарных должников = 1

    @XStreamAlias("num")
    private final StringElement num; //20.2. Число солидарных должников

    public Ul13JointDebtors() {
        this.exist0 = new StringElement("");
        this.exist1 = null;
        this.num = null;
    }

    public Ul13JointDebtors(Integer num) {
        this.exist0 = null;
        this.exist1 = new StringElement("");
        this.num = new StringElement(num.toString());
    }
}
