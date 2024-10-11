package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.List;

/**
 * Блок 24. Сведения о поручительстве
 */
public class Ul24Warranty {

    @XStreamAlias("warrantyFact_0")
    private final StringElement warrantyFact0; // 33.1. Признак наличия поручительства = 0

    @XStreamAlias("warrantyFact_1")
    private final StringElement warrantyFact1; // 33.1. Признак наличия поручительства = 1

    @XStreamImplicit(itemFieldName = "Uid_group_UL_24_Warranty")
    private final List<UidGroupUl24Warranty> uidGroupUl24WarrantyList; // Список поручительств

    public Ul24Warranty() {
        this.warrantyFact0 = new StringElement("");
        this.warrantyFact1 = null;
        this.uidGroupUl24WarrantyList = null;
    }

    public Ul24Warranty(List<UidGroupUl24Warranty> uidGroupUl24WarrantyList) {

        this.warrantyFact0 = null;
        this.warrantyFact1 = new StringElement("");

        this.uidGroupUl24WarrantyList = uidGroupUl24WarrantyList;
    }

    public List<UidGroupUl24Warranty> getUidGroupUl24WarrantyList() {
        return uidGroupUl24WarrantyList;
    }
}
