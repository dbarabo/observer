package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;
import java.util.List;

/**
 * Субъект стал принципалом по гарантии или поручителем по сделке
 */
public class UlEvent2_2_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_10_DealUid")
    final Ul10DealUid ul10DealUid; // Блок 10. Идентификатор сделки

    @XStreamAlias("UL_11_Deal")
    final Ul11Deal ul11Deal; // Блок 11. Общие сведения о сделке

    @XStreamAlias("UL_12_Amount")
    final Ul12Amount ul12Amount; // Блок 12. Сумма и валюта обязательства

    @XStreamImplicit(itemFieldName = "UL_12_1_AmountInfo")
    final List<Ul12_1AmountInfo> ul12_1AmountInfoList; //  maxOccurs="unbounded" Блок 19(1). Сведения об обеспечиваемом обязательстве

    @XStreamAlias("UL_14_PaymentTerms")
    final Ul14PaymentTerms ul14PaymentTerms; // Блок 14. Сведения об условиях платежей

    @XStreamAlias("UL_16_Fund")
    final Ul16Fund ul16Fund; // Блок 14. Сведения об условиях платежей

    @XStreamAlias("UL_17_18_19_20_Group")
    final Ul17_18_19_20Group ul17_18_19_20Group; // Блок 17-20

    @XStreamAlias("UL_44_Accounting")
    final Ul44Accounting ul44Accounting; // Блок 44. Сведения об учете обязательства

    @XStreamAlias("UL_46_Participation")
    final Ul46Participation ul46Participation; // Блок 46. Сведения об участии в обязательстве, по которому формируется кредитная история

    public UlEvent2_2_1(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul11Deal ul11Deal,
                      Ul12Amount ul12Amount, List<Ul12_1AmountInfo> ul121AmountInfoList,
                      Ul14PaymentTerms ul14PaymentTerms, Ul17_18_19_20Group ul17181920Group,
                      Ul16Fund ul16Fund, Ul44Accounting ul44Accounting,
                      Ul46Participation ul46Participation) {

        super(orderNum, eventDate);

        this.ul10DealUid = ul10DealUid;
        this.ul11Deal = ul11Deal;
        this.ul12Amount = ul12Amount;
        ul12_1AmountInfoList = ul121AmountInfoList;
        this.ul14PaymentTerms = ul14PaymentTerms;
        ul17_18_19_20Group = ul17181920Group;
        this.ul16Fund = ul16Fund;
        this.ul44Accounting = ul44Accounting;
        this.ul46Participation = ul46Participation;
    }
}
