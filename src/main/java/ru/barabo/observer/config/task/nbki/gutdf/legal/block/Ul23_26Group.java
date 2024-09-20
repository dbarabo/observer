package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.List;

/**
 * Блок 23,26
 */
public class Ul23_26Group {

    @XStreamAlias("assetKind_0")
    private final StringElement assetKind0; // 32.1. Признак наличия залога = 0

    @XStreamAlias("assetKind_1")
    private final StringElement assetKind1; // 32.1. Признак наличия залога = 1

    @XStreamImplicit(itemFieldName = "PropertyId_group_UL_23_26_Group")
    private final List<PropertyIdGroupUl23_26Group> propertyIdGroupUl23_26GroupList;

    public Ul23_26Group() {

        this.assetKind0 = new StringElement("");
        this.assetKind1 = null;

        propertyIdGroupUl23_26GroupList = null;
    }

    public Ul23_26Group(List<PropertyIdGroupUl23_26Group> propertyIdGroupUl23_26GroupList) {
        this.assetKind0 = null;

        this.assetKind1 = new StringElement("");

        this.propertyIdGroupUl23_26GroupList = propertyIdGroupUl23_26GroupList;
    }
}
