package ru.barabo.observer.config.task.p440.out.xml.ver4.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseDataVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.*;

@XStreamAlias("СПРОБОСТАТ")
public class RestInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("НаимПодтв")
    private String pbFile;

    @XStreamAlias("ВидСпр")
    private String viewHelp; // берем из базы

    @XStreamAlias("СведЗапрРеш")
    private RestInfoRequestDecision restInfoRequestDecision;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

    @XStreamAlias("КлиентБанк")
    private ClientVer4 client;

    @XStreamAlias("СведенияОст")
    private RestInfoDetailVer4 restInfoDetail;

    public RestInfoPartVer4(RestResponseDataVer4 restResponseData) {
        super(false);

        this.viewHelp = restResponseData.getViewHelp();

        this.pbFile = restResponseData.getPbFileName();

        this.client = ClientVer4.fromPayerXml(restResponseData.getPayer());

        this.restInfoRequestDecision = new RestInfoRequestDecision(restResponseData);

        this.restInfoDetail = new RestInfoDetailVer4(restResponseData);
    }
}
