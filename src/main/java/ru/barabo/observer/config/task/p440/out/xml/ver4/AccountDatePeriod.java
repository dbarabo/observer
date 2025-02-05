package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.RequestResponseData;

@XStreamAlias("СчДатаПериод")
public class AccountDatePeriod {

    @XStreamAlias("ДейстПоСост")
    private DateWorkStateOut onStateDate;

    @XStreamAlias("ИмПериод")
    private DatePeriodOut periodDate;

    public AccountDatePeriod(RequestResponseData requestResponse) {

        if(requestResponse.getOnStateDateRequest() != null) {
            onStateDate = new DateWorkStateOut(requestResponse.getOnStateDateRequest());
        }

        if(requestResponse.getEndPeriodRequest() != null) {
            periodDate = new DatePeriodOut(requestResponse.getStartPeriodRequest(), requestResponse.getEndPeriodRequest());
        }
    }
}
