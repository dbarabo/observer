package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseDataVer4;

@XStreamAlias("СведЗапрРеш")
public class RestInfoRequestDecision {

    @XStreamAlias("СведЗапр")
    private InfoRequestDetail infoRequestDetail;

    @XStreamAlias("СведРеш")
    private InfoDecisionDetail infoDecisionDetail;

    public RestInfoRequestDecision(RestResponseDataVer4 restResponseData) {

        if("RPO".equalsIgnoreCase(restResponseData.fileNameFromFns().substring(0,3)) ) {
            infoDecisionDetail = new InfoDecisionDetail(restResponseData);
        } else {
            infoRequestDetail = new InfoRequestDetail(restResponseData);
        }
    }
}
