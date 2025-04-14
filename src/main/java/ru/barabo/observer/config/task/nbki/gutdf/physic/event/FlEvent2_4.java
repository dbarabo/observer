package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

import java.util.Date;

/**
 * Изменились сведения об обеспечении исполнения обязательства
 */
public class FlEvent2_4 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid; // Блок 17. Идентификатор сделки

    @XStreamAlias("FL_32_35_Group")
    final Fl32_35Group fl32_35Group; // Блок 32,35

    @XStreamAlias("FL_33_Warranty")
    final Fl33Warranty fl33Warranty; // Блок 33. Сведения о поручительстве

    @XStreamAlias("FL_34_Guarantee")
    final Fl34Guarantee fl34Guarantee; // Блок 34. Сведения о независимой гарантии

    @XStreamAlias("FL_36_ProvisionPayment")
    final Fl36ProvisionPayment fl36ProvisionPayment; // Блок 36. Сведения о погашении требований кредитора по обязательству за счет обеспечения

    public FlEvent2_4(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, Fl32_35Group fl3235Group,
                      Fl33Warranty fl33Warranty, Fl34Guarantee fl34Guarantee,
                      Fl36ProvisionPayment fl36ProvisionPayment) {

        super(orderNum, eventDate, "2.4");

        this.fl17DealUid = fl17DealUid;
        fl32_35Group = fl3235Group;
        this.fl33Warranty = fl33Warranty;
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

    public Fl32_35Group getFl32_35Group() {
        return fl32_35Group;
    }

    public Fl33Warranty getFl33Warranty() {
        return fl33Warranty;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "2.4";
    }
}
