package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;
import java.util.List;

/**
 * Обязательство субъекта прекратилось
 */
public class UlEvent2_5 extends AbstractEventData {

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

    @XStreamAlias("UL_17_18_19_20_Group")
    final Ul17_18_19_20Group ul17_18_19_20Group; // Блок 17-20

    @XStreamAlias("UL_29_ContractEnd")
    final Ul29ContractEnd ul29ContractEnd; // Блок 29. Сведения о прекращении обязательства

    @XStreamAlias("UL_46_Participation")
    final Ul46Participation ul46Participation; // Блок 46. Сведения об участии в обязательстве, по которому формируется кредитная история

    public UlEvent2_5(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul11Deal ul11Deal,
                      Ul12Amount ul12Amount, List<Ul12_1AmountInfo> ul121AmountInfoList,
                      Ul14PaymentTerms ul14PaymentTerms, Ul17_18_19_20Group ul17181920Group,
                      Ul29ContractEnd ul29ContractEnd, Ul46Participation ul46Participation) {

        super(orderNum, eventDate, "2.5");

        this.ul10DealUid = ul10DealUid;
        this.ul11Deal = ul11Deal;
        this.ul12Amount = ul12Amount;
        ul12_1AmountInfoList = ul121AmountInfoList;
        this.ul14PaymentTerms = ul14PaymentTerms;
        ul17_18_19_20Group = ul17181920Group;

        this.ul29ContractEnd = ul29ContractEnd;
        this.ul46Participation = ul46Participation;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.5";
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

    public Ul17_18_19_20Group getUl17_18_19_20Group() {
        return ul17_18_19_20Group;
    }

    public Ul29ContractEnd getUl29ContractEnd() {
        return ul29ContractEnd;
    }

    public Ul46Participation getUl46Participation() {
        return ul46Participation;
    }
}
