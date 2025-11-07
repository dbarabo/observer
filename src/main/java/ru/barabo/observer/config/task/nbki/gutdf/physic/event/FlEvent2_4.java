package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul23_26Group;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;
import java.util.List;

/**
 * Изменились сведения об обеспечении исполнения обязательства
 */
public class FlEvent2_4 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid; // Блок 17. Идентификатор сделки

    @XStreamImplicit(itemFieldName = "FL_32_35_Group")
    private final List<Fl32_35Group> fl3235GroupList; // Блок 32,35

    @XStreamImplicit(itemFieldName = "FL_33_Warranty")
    final List<Fl33Warranty> fl33WarrantyList; // Блок 33. Сведения о поручительстве

    @XStreamAlias("FL_34_Guarantee")
    final Fl34Guarantee fl34Guarantee; // Блок 34. Сведения о независимой гарантии

    @XStreamAlias("FL_36_ProvisionPayment")
    final Fl36ProvisionPayment fl36ProvisionPayment; // Блок 36. Сведения о погашении требований кредитора по обязательству за счет обеспечения

    public FlEvent2_4(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, List<Fl32_35Group> fl3235GroupList,
                      List<Fl33Warranty> fl33WarrantyList, Fl34Guarantee fl34Guarantee,
                      Fl36ProvisionPayment fl36ProvisionPayment) {

        super(orderNum, eventDate, "2.4");

        this.fl17DealUid = fl17DealUid;
        this.fl3235GroupList = fl3235GroupList;
        this.fl33WarrantyList = fl33WarrantyList;
        this.fl34Guarantee = fl34Guarantee;
        this.fl36ProvisionPayment = fl36ProvisionPayment;
    }

    @Override
    public String getUnicalId() {
        return fl17DealUid.getUid().value;
    }

    public Fl17DealUid getFl17DealUid() {
        return fl17DealUid;
    }

    public List<Fl33Warranty> getFl33WarrantyList() {
        return fl33WarrantyList;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.4";
    }

    public List<Fl32_35Group> getFl3235GroupList() {
        return fl3235GroupList;
    }
}
