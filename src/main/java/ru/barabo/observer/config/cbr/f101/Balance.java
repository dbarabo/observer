package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Баланс")
public class Balance extends RowDataBalance {

    public List<Balance> getBalanceList() {
        return balanceList;
    }

    @XStreamImplicit(itemFieldName="Баланс")
    private  List<Balance> balanceList;
}
