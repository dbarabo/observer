package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;

@XStreamAlias("СПРОБОСТАТ")
public class RestInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("НаимПодтв")
    private String pbFile;

    @XStreamAlias("ВидСпр")
    private String viewHelp; // берем из базы

    @XStreamAlias("СведЗапрРеш")
    private RestInfoRequestDecision restInfoRequestDecision;

    @XStreamAlias("СведенияОст")
    private RestInfoDetailVer4 restInfoDetail;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public RestInfoPartVer4(RestResponseData restResponseData) {
        super(false, ClientVer4.fromPayerXml(restResponseData.getPayer()));

        this.viewHelp = restResponseData.getViewHelp();

        this.pbFile = restResponseData.getPbFileName();

        this.restInfoRequestDecision = new RestInfoRequestDecision(restResponseData);

        this.restInfoDetail = new RestInfoDetailVer4(restResponseData);
    }
}
