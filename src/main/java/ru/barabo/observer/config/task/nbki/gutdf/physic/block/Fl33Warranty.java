package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 33. Сведения о поручительствах
 */
public class Fl33Warranty {

    @XStreamAlias("warrantyFact_0")
    private final StringElement warrantyFact0; // 33.1. Признак наличия поручительства = 0

    @XStreamAlias("warrantyFact_1")
    private final StringElement warrantyFact1; // 33.1. Признак наличия поручительства = 1

    @XStreamImplicit(itemFieldName = "Uid_group_FL_33_Warranty")
    private final UidGroupFl33Warranty uidGroupFl33WarrantyList; // Список поручительств

    public Fl33Warranty() {
        this.warrantyFact0 = new StringElement("");
        this.warrantyFact1 = null;
        this.uidGroupFl33WarrantyList = null;
    }

    public Fl33Warranty(UidGroupFl33Warranty uidGroupFl33WarrantyList) {

        this.warrantyFact0 = null;
        this.warrantyFact1 = new StringElement("");

        this.uidGroupFl33WarrantyList = uidGroupFl33WarrantyList;
    }
}
