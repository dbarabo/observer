package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 23(1). Сведения об изменении условий обязательства в результате наступления указанного в сделке события
 */
public class Fl23_1ContractTermsChanges {

    @XStreamAlias("changeFact_0")
    private final StringElement changeFact0; // 23(1).1. Признак изменения условий обязательства = 0

    @XStreamAlias("changeFact_1")
    private final StringElement changeFact1; // 23(1).1. Признак изменения условий обязательства = 1

    @XStreamAlias("termsChangeCode")
    private final StringElement termsChangeCode; // 23(1).2. Код вида изменения условий обязательства

    @XStreamAlias("termsChangeDesc")
    private final StringElement termsChangeDesc; // minOccurs="0" 23(1).3. Описание иного изменения условий обязательства

    @XStreamAlias("changingDate")
    private final StringElement changingDate; // emptyValueType| 23(1).4. Дата начала действия изменения условий обязательства

    public Fl23_1ContractTermsChanges() {
        this.changeFact0 = new StringElement("");
        this.changeFact1 = null;
        this.termsChangeCode = null;
        this.termsChangeDesc = null;
        this.changingDate = null;
    }

    public Fl23_1ContractTermsChanges(Integer termsChangeCode, String termsChangeDesc, Date changingDate) {
        this.changeFact0 =  null;
        this.changeFact1 = new StringElement("");

        this.termsChangeCode = new StringElement(termsChangeCode.toString());

        this.termsChangeDesc = termsChangeDesc == null ? null : new StringElement(termsChangeDesc);

        this.changingDate = new StringElement(changingDate == null ? "-" : XmlLoader.formatDate(changingDate));
    }
}
