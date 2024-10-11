package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;

/**
 * Изменились сведения об обеспечении исполнения обязательства
 */
public class UlEvent2_4 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_10_DealUid")
    final Ul10DealUid ul10DealUid; // Блок 10. Идентификатор сделки

    @XStreamAlias("UL_23_26_Group")
    final Ul23_26Group ul23_26Group; // Блок 23,26

    @XStreamAlias("UL_24_Warranty")
    final Ul24Warranty ul24Warranty; // Блок 24. Сведения о поручительстве

    @XStreamAlias("UL_25_Guarantee")
    final Ul25Guarantee ul25Guarantee; // Блок 25. Сведения о независимой гарантии

    @XStreamAlias("UL_27_ProvisionPayment")
    final Ul27ProvisionPayment ul27ProvisionPayment; // Блок 27. Сведения о погашении требований кредитора по обязательству за счет обеспечения

    public UlEvent2_4(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul23_26Group ul2326Group,
                      Ul24Warranty ul24Warranty, Ul25Guarantee ul25Guarantee,
                      Ul27ProvisionPayment ul27ProvisionPayment) {

        super(orderNum, eventDate);

        this.ul10DealUid = ul10DealUid;
        ul23_26Group = ul2326Group;
        this.ul24Warranty = ul24Warranty;
        this.ul25Guarantee = ul25Guarantee;
        this.ul27ProvisionPayment = ul27ProvisionPayment;
    }

    @Override
    public String getEvent() {
        return "2.4";
    }

    @Override
    public String getUnicalId() {
        return ul10DealUid.getUid().value;
    }

    public Ul10DealUid getUl10DealUid() {
        return ul10DealUid;
    }

    public Ul23_26Group getUl23_26Group() {
        return ul23_26Group;
    }

    public Ul24Warranty getUl24Warranty() {
        return ul24Warranty;
    }
}

