package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 44. Сведения об учете обязательства
 */
public class Ul44Accounting {

    @XStreamAlias("exist_0")
    private final StringElement exist0; //54.1. Признак учета обязательства = 0

    @XStreamAlias("sum")
    private final StringElement sum; //54.3. Сумма обязательства, учтенная на внебалансовых счетах

    @XStreamAlias("exist_1")
    private final StringElement exist1; //54.1. Признак учета обязательства = 1

    //minOccurs="0"  на группу minInterest-maxInterest
    @XStreamAlias("rate")
    private final StringElement rate; //dec4p2Type 44.2. Процентная ставка

    @XStreamAlias("supportExist_0")
    private final StringElement supportExist0; //54.4. Признак льготного финансирования с государственной поддержкой = 0

    @XStreamAlias("supportExist_1")
    private final StringElement supportExist1; //54.4. Признак льготного финансирования с государственной поддержкой = 1

    @XStreamAlias("supportInfo")
    private final StringElement supportInfo; //54.5. Информация о программе государственной поддержки

    @XStreamAlias("calcDate")
    private final StringElement calcDate; //54.7. Дата расчета

    public Ul44Accounting(Number offBalanceAmount, Number rate, String supportInfo,
                          Date calcDate) {

        this.exist0 = offBalanceAmount == null ? null : new StringElement("");
        this.sum = offBalanceAmount == null ? null : new StringElement(XmlLoader.formatSum(offBalanceAmount));

        this.exist1 = offBalanceAmount != null ? null : new StringElement("");;
        this.rate = new StringElement(rate == null ? "0.00" : XmlLoader.formatSum(rate));
        supportExist0 = supportInfo != null ? null : new StringElement("");
        supportExist1 = supportInfo == null ? null : new StringElement("");
        this.supportInfo = new StringElement(supportInfo);
        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));
    }
}
