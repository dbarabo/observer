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

    @XStreamAlias("propertyId")
    private final StringElement propertyId; // emptyValueType| Str 32.3., 35.9. Идентификационный код предмета залога

    @XStreamAlias("UL_23_Collateral")
    private final Ul23Collateral ul23Collateral; // Блок 32. Сведения о залоге

    @XStreamImplicit(itemFieldName = "UL_26_Insurance")
    private final List<Ul26Insurance> ul26Insurance; // Блок 26. Сведения о страховании предмета залога

    public Ul23_26Group() {

        this.assetKind0 = new StringElement("");
        this.assetKind1 = null;

        propertyId = null;

        ul23Collateral = null;
        ul26Insurance = null;
    }

    public Ul23_26Group(String propertyId, Ul23Collateral ul23Collateral,
                        List<Ul26Insurance> ul26Insurance) {
        this.assetKind0 = null;
        this.assetKind1 = new StringElement("");

        this.propertyId = new StringElement(propertyId);

        this.ul23Collateral = ul23Collateral;
        this.ul26Insurance = ul26Insurance;
    }

    public StringElement getPropertyId() {
        return propertyId;
    }

    public Ul23Collateral getUl23Collateral() {
        return ul23Collateral;
    }

    public List<Ul26Insurance> getUl26Insurance() {
        return ul26Insurance;
    }
}
