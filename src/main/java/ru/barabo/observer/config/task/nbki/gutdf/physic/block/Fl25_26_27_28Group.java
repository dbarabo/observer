package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 25-28
 */
public class Fl25_26_27_28Group {

    // прошло 30 календарных дней с даты последнего расчета суммы задолженности по показателю 17.8
    @XStreamAlias("lastPayExist_0")
    private final StringElement lastPayExist0; // minOccurs="0" 25.3., 26.2., 27.2. Признак расчета по последнему платежу = 0

    // субъект внес платеж либо наступил срок для внесения платежа по срочному долгу
    @XStreamAlias("lastPayExist_1")
    private final StringElement lastPayExist1; // minOccurs="0" 25.3., 26.2., 27.2. Признак расчета по последнему платежу = 1

    @XStreamAlias("calcDate")
    private final StringElement calcDate; // 25.8., 26.7., 27.7., 28.13. Дата расчета

    @XStreamAlias("FL_25_Debt")
    private final Fl25Debt fl25Debt; // Блок 25. Сведения о задолженности

    @XStreamAlias("FL_26_DebtDue")
    private final Fl26DebtDue fl26DebtDue; // Блок 26. Сведения о срочной задолженности

    @XStreamAlias("FL_27_DebtOverdue")
    private final Fl27DebtOverdue fl27DebtOverdue; // Блок 27. Сведения о просроченной задолженности

    @XStreamAlias("FL_28_Payment")
    private final Fl28Payment fl28Payment; // Блок 28. Сведения о внесении платежей

    public Fl25_26_27_28Group(Boolean isLastPayExist, Date calcDate, Fl25Debt fl25Debt, Fl26DebtDue fl26DebtDue,
                              Fl27DebtOverdue fl27DebtOverdue, Fl28Payment fl28Payment) {
        this.lastPayExist0 = (isLastPayExist != null && !isLastPayExist) ? new StringElement("") : null;
        this.lastPayExist1 = (isLastPayExist != null && isLastPayExist) ? new StringElement("") : null;

        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));

        this.fl25Debt = fl25Debt;
        this.fl26DebtDue = fl26DebtDue;
        this.fl27DebtOverdue = fl27DebtOverdue;
        this.fl28Payment = fl28Payment;
    }

    public Boolean isLastPayExist() {
        if(lastPayExist1 == null && lastPayExist0 == null) return null;

        return lastPayExist1 != null;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }

    public Fl25Debt getFl25Debt() {
        return fl25Debt;
    }

    public Fl26DebtDue getFl26DebtDue() {
        return fl26DebtDue;
    }

    public Fl27DebtOverdue getFl27DebtOverdue() {
        return fl27DebtOverdue;
    }

    public Fl28Payment getFl28Payment() {
        return fl28Payment;
    }
}
