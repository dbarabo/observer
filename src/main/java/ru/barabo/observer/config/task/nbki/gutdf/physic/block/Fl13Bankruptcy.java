package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 13. Сведения по делу о несостоятельности (банкротстве)
 */
public class Fl13Bankruptcy {

    @XStreamAlias("bankruptcyFact_0")
    private final StringElement bankruptcyFact0; // 13.1. Признак дела о банкротстве = 0

    @XStreamAlias("bankruptcyFact_1")
    private final StringElement bankruptcyFact1; // 13.1. Признак дела о банкротстве = 1

    // TODO

    public Fl13Bankruptcy() {
        this.bankruptcyFact0 = new StringElement("");
        this.bankruptcyFact1 = null;
    }
}
