package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Срочные")
public class TimeAccount extends RowDataBalance {

    public List<TimeAccount> getTimeAccountList() {
        return timeAccountList;
    }

    @XStreamImplicit(itemFieldName="Срочные")
    private List<TimeAccount> timeAccountList;
}
