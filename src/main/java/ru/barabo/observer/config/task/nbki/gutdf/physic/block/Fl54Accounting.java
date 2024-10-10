package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 54. Сведения об учете обязательства
 */
public class Fl54Accounting {

    @XStreamAlias("exist_0")
    private final StringElement exist0; //54.1. Признак учета обязательства = 0

    @XStreamAlias("sum")
    private final StringElement sum; //54.3. Сумма обязательства, учтенная на внебалансовых счетах

    @XStreamAlias("exist_1")
    private final StringElement exist1; //54.1. Признак учета обязательства = 1

    //minOccurs="0"  на группу minInterest-maxInterest
    @XStreamAlias("minInterest")
    private final StringElement minInterest; //dec4p2Type 54.2. Минимальная процентная ставка если нет=0.00

    @XStreamAlias("maxInterest")
    private final StringElement maxInterest; //dec4p2Type 4.6. Максимальная процентная ставка

    @XStreamAlias("supportExist_0")
    private final StringElement supportExist0; //54.4. Признак льготного финансирования с государственной поддержкой = 0

    @XStreamAlias("supportExist_1")
    private final StringElement supportExist1; //54.4. Признак льготного финансирования с государственной поддержкой = 1

    @XStreamAlias("supportInfo")
    private final StringElement supportInfo; //54.5. Информация о программе государственной поддержки

    @XStreamAlias("calcDate")
    private final StringElement calcDate; //54.7. Дата расчета

    public Fl54Accounting(Number offBalanceAmount, Number minInterest, Number maxInterest, String supportInfo,
                          Date calcDate) {

        this.exist0 = offBalanceAmount == null ? null : new StringElement("");
        this.sum = offBalanceAmount == null ? null : new StringElement(XmlLoader.formatSum(offBalanceAmount));

        this.exist1 = offBalanceAmount != null ? null : new StringElement("");

        this.minInterest = new StringElement(minInterest == null ? "0.00" : XmlLoader.formatSum(minInterest));

        this.maxInterest = new StringElement(maxInterest == null ? "0.00" : XmlLoader.formatSum(maxInterest));

        supportExist0 = supportInfo != null ? null : new StringElement("");

        supportExist1 = supportInfo == null ? null : new StringElement("");

        this.supportInfo = supportInfo == null ? null : new StringElement(supportInfo);

        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));
    }

    public StringElement getSum() {
        return sum;
    }

    public StringElement getMinInterest() {
        return minInterest;
    }

    public StringElement getMaxInterest() {
        return maxInterest;
    }

    public StringElement getSupportInfo() {
        return supportInfo;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }
}
