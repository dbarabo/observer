package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 17-20
 */
public class Ul17_18_19_20Group {

    // прошло 30 календарных дней с даты последнего расчета суммы задолженности по показателю 17.8
    @XStreamAlias("lastPayExist_0")
    private final StringElement lastPayExist0; // minOccurs="0" 25.3., 26.2., 27.2. Признак расчета по последнему платежу = 0

    // субъект внес платеж либо наступил срок для внесения платежа по срочному долгу
    @XStreamAlias("lastPayExist_1")
    private final StringElement lastPayExist1; // minOccurs="0" 25.3., 26.2., 27.2. Признак расчета по последнему платежу = 1

    @XStreamAlias("calcDate")
    private final StringElement calcDate; // 25.8., 26.7., 27.7., 28.13. Дата расчета

    @XStreamAlias("UL_17_Debt")
    private final Ul17Debt ul17Debt; // Блок 25. Сведения о задолженности

    @XStreamAlias("UL_18_DebtDue")
    private final Ul18DebtDue ul18DebtDue; // Блок 18. Сведения о срочной задолженности

    @XStreamAlias("UL_19_DebtOverdue")
    private final Ul19DebtOverdue ul19DebtOverdue; // Блок 19. Сведения о просроченной задолженности

    @XStreamAlias("UL_20_Payment")
    private final Ul20Payment ul20Payment; // Блок 20. Сведения о внесении платежей

    public Ul17_18_19_20Group(Boolean isLastPayExist, Date calcDate, Ul17Debt ul17Debt,
                              Ul18DebtDue ul18DebtDue, Ul19DebtOverdue ul19DebtOverdue, Ul20Payment ul20Payment) {

        this.lastPayExist0 = (isLastPayExist != null && !isLastPayExist) ? new StringElement("") : null;
        this.lastPayExist1 = (isLastPayExist != null && isLastPayExist) ? new StringElement("") : null;

        this.calcDate = new StringElement(XmlLoader.formatDate(calcDate));

        this.ul17Debt = ul17Debt;
        this.ul18DebtDue = ul18DebtDue;
        this.ul19DebtOverdue = ul19DebtOverdue;
        this.ul20Payment = ul20Payment;
    }

    public Boolean isLastPay() {

        if(lastPayExist1 == null && lastPayExist0 == null) return null;

        return lastPayExist1 != null;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }

    public Ul17Debt getUl17Debt() {
        return ul17Debt;
    }

    public Ul18DebtDue getUl18DebtDue() {
        return ul18DebtDue;
    }

    public Ul19DebtOverdue getUl19DebtOverdue() {
        return ul19DebtOverdue;
    }

    public Ul20Payment getUl20Payment() {
        return ul20Payment;
    }
}
