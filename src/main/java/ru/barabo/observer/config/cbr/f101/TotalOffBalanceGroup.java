package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ИтогВнебал")
public class TotalOffBalanceGroup {

    public List<TotalOffBalance> getTotalOffBalanceList() {
        return totalOffBalanceList;
    }

    @XStreamImplicit(itemFieldName="ИтогоВнебалСч")
    private List<TotalOffBalance> totalOffBalanceList;
}
