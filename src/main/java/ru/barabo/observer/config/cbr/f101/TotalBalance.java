package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ИтогоБаланс")
public class TotalBalance extends RowDataBalance {

    public List<TotalBalance> getBalanceList() {
        return balanceList;
    }

    @XStreamImplicit(itemFieldName="ИтогоБаланс")
    private List<TotalBalance> balanceList;
}
