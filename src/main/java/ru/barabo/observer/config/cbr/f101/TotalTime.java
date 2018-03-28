package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ИтогоСрочные")
public class TotalTime extends RowDataBalance {

    public List<TotalTime> getTotalTimeList() {
        return totalTimeList;
    }

    @XStreamImplicit(itemFieldName="ИтогоСрочные")
    private List<TotalTime> totalTimeList;
}
