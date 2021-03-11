package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("СведРеш")
public class InfoDecisionDetail {

    @XStreamAlias("НомРеш")
    private String numberDecision;

    @XStreamAlias("ДатаРеш")
    private String dateDecision;

    @XStreamAlias("КодНОРеш")
    private String codeFnsDecision;

    public InfoDecisionDetail(RestResponseDataVer4 restResponseData) {

        numberDecision = restResponseData.getNumberRequest();

        dateDecision = XmlLoader.formatDate(restResponseData.getDateRequest());

        codeFnsDecision = restResponseData.getFns().getFnsCode();
    }
}
