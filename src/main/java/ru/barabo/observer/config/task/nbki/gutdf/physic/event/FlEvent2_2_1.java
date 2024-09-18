package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;
import java.util.List;

/**
 * Субъект стал принципалом по гарантии или поручителем по сделке
 */
public class FlEvent2_2_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid; // Блок 17. Идентификатор сделки

    @XStreamAlias("FL_18_Deal")
    final Fl18Deal fl18Deal; // Блок 18. Общие сведения о сделке

    @XStreamAlias("FL_19_Amount")
    final Fl19Amount fl19Amount; // Блок 19. Сумма и валюта обязательства

    @XStreamImplicit(itemFieldName = "FL_19_1_AmountInfo")
    final List<Fl19_1AmountInfo> fl19_1AmountInfoList; //  maxOccurs="unbounded" Блок 19(1). Сведения об обеспечиваемом обязательстве

    @XStreamAlias("FL_21_PaymentTerms")
    final Fl21PaymentTerms fl21PaymentTerms; // Блок 21. Сведения об условиях платежей

    @XStreamAlias("FL_24_Fund")
    final Fl24Fund fl24Fund; // Блок 24. Дата передачи финансирования субъекту или возникновения обеспечения исполнения обязательства

    @XStreamAlias("FL_54_Accounting")
    final Fl54Accounting fl54Accounting; // Блок 54. Сведения об учете обязательства

    @XStreamAlias("FL_56_Participation")
    final Fl56Participation fl56Participation; // Блок 56. Сведения об участии в обязательстве, по которому формируется кредитная история

    public FlEvent2_2_1(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, Fl18Deal fl18Deal,
                        Fl19Amount fl19Amount, List<Fl19_1AmountInfo> fl191AmountInfoList,
                        Fl21PaymentTerms fl21PaymentTerms, Fl24Fund fl24Fund, Fl54Accounting fl54Accounting,
                        Fl56Participation fl56Participation) {

        super(orderNum, eventDate);

        this.fl17DealUid = fl17DealUid;
        this.fl18Deal = fl18Deal;
        this.fl19Amount = fl19Amount;
        fl19_1AmountInfoList = fl191AmountInfoList;
        this.fl21PaymentTerms = fl21PaymentTerms;
        this.fl24Fund = fl24Fund;
        this.fl54Accounting = fl54Accounting;
        this.fl56Participation = fl56Participation;
    }
}
