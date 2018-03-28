package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Внебал")
public class OffBalanceGroup {

    public List<OffBalance> getOffBalanceList() {
        return offBalanceList;
    }

    @XStreamImplicit(itemFieldName="ВнебалСч")
    private List<OffBalance> offBalanceList;
}
