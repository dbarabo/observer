package ru.barabo.observer.config.task.cbr.extract.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class RequestVko {

    @XStreamAlias("РегНом")
    public String regNumber;

    @XStreamAlias("ДатаНачПериод")
    public String startPeriod;

    @XStreamAlias("ДатаОкПериод")
    public String endPeriod;

    @XStreamAlias("ВидЗапрИнф")
    public Integer typeRequest;

    @XStreamAlias("ДатаВремНапрЗапр")
    public String sentRequest;

    @XStreamAlias("ИндЗапр")
    public String idRequest;

    @XStreamAlias("ПризнПлКарт")
    public String isCardInfo;

    @XStreamAlias("ДатаСрокОтвета")
    public String dueResponse;

    @XStreamImplicit(itemFieldName = "ФайлЗапросВКОСписИдентЗапр")
    public List<RequestVkoListIdRequest> requestVkoListIdRequest;
}
