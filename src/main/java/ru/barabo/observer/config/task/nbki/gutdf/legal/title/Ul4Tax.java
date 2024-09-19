package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 4. Номер налогоплательщика
 */
public class Ul4Tax {

    @XStreamAlias("UL_4_Tax_current")
    private final Ul4TaxType ul4TaxCurrent; //

    @XStreamAlias("UL_4_Tax_new")
    private final Ul4TaxType ul4TaxNew; // minOccurs="0"

    public Ul4Tax(Ul4TaxType ul4TaxCurrent, Ul4TaxType ul4TaxNew) {

        this.ul4TaxCurrent = ul4TaxCurrent;

        this.ul4TaxNew = ul4TaxNew;
    }
}
