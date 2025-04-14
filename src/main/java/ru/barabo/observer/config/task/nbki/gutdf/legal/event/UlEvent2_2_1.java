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

    @XStreamAlias("UL_44_Accounting")
    final Ul44Accounting ul44Accounting; // Блок 44. Сведения об учете обязательства

    @XStreamAlias("UL_46_Participation")
    final Ul46Participation ul46Participation; // Блок 46. Сведения об участии в обязательстве, по которому формируется кредитная история

    public UlEvent2_2_1(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul11Deal ul11Deal,
                      Ul12Amount ul12Amount, List<Ul12_1AmountInfo> ul121AmountInfoList,
                      Ul14PaymentTerms ul14PaymentTerms, Ul16Fund ul16Fund, Ul44Accounting ul44Accounting,
                      Ul46Participation ul46Participation) {

        super(orderNum, eventDate, "2.2.1");

        this.ul10DealUid = ul10DealUid;
        this.ul11Deal = ul11Deal;
        this.ul12Amount = ul12Amount;
        ul12_1AmountInfoList = ul121AmountInfoList;
        this.ul14PaymentTerms = ul14PaymentTerms;
        this.ul16Fund = ul16Fund;
        this.ul44Accounting = ul44Accounting;
        this.ul46Participation = ul46Participation;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.2.1";
    }

    @Override
    public String getUnicalId() {
        return ul10DealUid.getUid().value;
    }

    public Ul10DealUid getUl10DealUid() {
        return ul10DealUid;
    }

    public Ul11Deal getUl11Deal() {
        return ul11Deal;
    }

    public Ul12Amount getUl12Amount() {
        return ul12Amount;
    }

    public List<Ul12_1AmountInfo> getUl12_1AmountInfoList() {
        return ul12_1AmountInfoList;
    }

    public Ul14PaymentTerms getUl14PaymentTerms() {
        return ul14PaymentTerms;
    }

    public Ul16Fund getUl16Fund() {
        return ul16Fund;
    }

    public Ul44Accounting getUl44Accounting() {
        return ul44Accounting;
    }

    public Ul46Participation getUl46Participation() {
        return ul46Participation;
    }
}
