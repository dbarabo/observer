package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.*;

import java.util.Date;
import java.util.List;

/**
 * Изменились сведения об обеспечении исполнения обязательства
 */
public class UlEvent2_4 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_10_DealUid")
    final Ul10DealUid ul10DealUid; // Блок 10. Идентификатор сделки

    @XStreamImplicit(itemFieldName = "UL_23_26_Group")
    private final List<Ul23_26Group> ul2326GroupList; // Блок 23,26

    @XStreamImplicit(itemFieldName = "UL_24_Warranty")
    final List<Ul24Warranty> ul24WarrantyList; // Блок 24. Сведения о поручительстве

    @XStreamAlias("UL_25_Guarantee")
    final Ul25Guarantee ul25Guarantee; // Блок 25. Сведения о независимой гарантии

    @XStreamAlias("UL_27_ProvisionPayment")
    final Ul27ProvisionPayment ul27ProvisionPayment; // Блок 27. Сведения о погашении требований кредитора по обязательству за счет обеспечения

    public UlEvent2_4(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, List<Ul23_26Group> ul2326GroupList,
                      List<Ul24Warranty> ul24WarrantyList, Ul25Guarantee ul25Guarantee,
                      Ul27ProvisionPayment ul27ProvisionPayment) {

        super(orderNum, eventDate, "2.4");

        this.ul10DealUid = ul10DealUid;
        this.ul2326GroupList = ul2326GroupList;
        this.ul24WarrantyList = ul24WarrantyList;
        this.ul25Guarantee = ul25Guarantee;
        this.ul27ProvisionPayment = ul27ProvisionPayment;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.4";
    }

    @Override
    public String getUnicalId() {
        return ul10DealUid.getUid().value;
    }

    public Ul10DealUid getUl10DealUid() {
        return ul10DealUid;
    }

    public List<Ul23_26Group> getUl23_26GroupList() {
        return ul2326GroupList;
    }

    public List<Ul24Warranty> getUl24WarrantyList() {
        return ul24WarrantyList;
    }
}

