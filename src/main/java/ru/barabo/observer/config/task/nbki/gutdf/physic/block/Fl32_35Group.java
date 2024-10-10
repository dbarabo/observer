package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.List;

/**
 * Блок 32,35
 */
public class Fl32_35Group {

    @XStreamAlias("assetKind_0")
    private final StringElement assetKind0; // 32.1. Признак наличия залога = 0

    @XStreamAlias("assetKind_1")
    private final StringElement assetKind1; // 32.1. Признак наличия залога = 1

    @XStreamImplicit(itemFieldName = "PropertyId_group_FL_32_35_Group")
    private final List<PropertyIdGroupFl32_35Group> propertyIdGroupFl32_35GroupList;

    public Fl32_35Group() {

        this.assetKind0 = new StringElement("");
        this.assetKind1 = null;

        propertyIdGroupFl32_35GroupList = null;
    }

    public Fl32_35Group(List<PropertyIdGroupFl32_35Group> propertyIdGroupFl3235GroupList) {
        this.assetKind0 = null;

        this.assetKind1 = new StringElement("");

        propertyIdGroupFl32_35GroupList = propertyIdGroupFl3235GroupList;
    }

    public List<PropertyIdGroupFl32_35Group> getPropertyIdGroupFl32_35GroupList() {
        return propertyIdGroupFl32_35GroupList;
    }
}
