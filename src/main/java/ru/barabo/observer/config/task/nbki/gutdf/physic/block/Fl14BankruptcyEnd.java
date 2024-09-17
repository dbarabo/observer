package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 14. Сведения о завершении расчетов с кредиторами и освобождении субъекта от исполнения обязательств в связи с банкротством
 */
public class Fl14BankruptcyEnd {

    @XStreamAlias("bankruptcyEndFact_0")
    private final StringElement bankruptcyEndFact0; //14.1. Признак завершения расчетов с кредиторами = 0

    @XStreamAlias("bankruptcyEndFact_1")
    private final StringElement bankruptcyEndFact1; //14.1. Признак завершения расчетов с кредиторами = 1

    // TODO

    public Fl14BankruptcyEnd() {
        this.bankruptcyEndFact0 = new StringElement("");
        this.bankruptcyEndFact1 = null;
    }
}
