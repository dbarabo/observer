package ru.barabo.observer.config.task.cbr.extract.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class RequestVkoListIdRequest {

    @XStreamAlias("ЗначСпис")
    public String clientValue;

    @XStreamImplicit(itemFieldName = "ФайлЗапросВКОПерЗначСпис")
    public List<RequestVkoPeriodList> requestVkoPeriodList;
}
