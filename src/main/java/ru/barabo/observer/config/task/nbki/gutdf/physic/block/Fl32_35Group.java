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

    @XStreamAlias("propertyId")
    private final StringElement propertyId; // emptyValueType| Str 32.3., 35.9. Идентификационный код предмета залога

    @XStreamAlias("FL_32_Collateral")
    private final Fl32Collateral fl32Collateral; // Блок 32. Сведения о залоге

    @XStreamImplicit(itemFieldName = "FL_35_Insurance")
    private final List<Fl35Insurance> fl35InsuranceList; // Блок 35. Сведения о страховании предмета залога

    public Fl32_35Group() {

        this.assetKind0 = new StringElement("");
        this.assetKind1 = null;

        propertyId = null;

        fl32Collateral = null;
        fl35InsuranceList = null;
    }

    public Fl32_35Group(String propertyId, Fl32Collateral fl32Collateral, List<Fl35Insurance> fl35InsuranceList) {
        this.assetKind0 = null;
        this.assetKind1 = new StringElement("");

        this.propertyId = new StringElement(propertyId);

        this.fl32Collateral = fl32Collateral;
        this.fl35InsuranceList = fl35InsuranceList;
    }

    public StringElement getPropertyId() {
        return propertyId;
    }

    public Fl32Collateral getFl32Collateral() {
        return fl32Collateral;
    }

    public List<Fl35Insurance> getFl35InsuranceList() {
        return fl35InsuranceList;
    }
}
