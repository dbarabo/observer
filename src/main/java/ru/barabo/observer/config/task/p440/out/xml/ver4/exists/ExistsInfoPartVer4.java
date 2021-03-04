package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.out.xml.ver4.*;

@XStreamAlias("СПРБННАЛИЧ")
public class ExistsInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("СведЗапр")
    private InfoRequestDetail infoRequestDetail;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

    @XStreamAlias("КлиентБанк")
    private ClientVer4 client;

    @XStreamAlias("СведСчет")
    private RestInfoDetailVer4 existsInfoDetail;

    public ExistsInfoPartVer4(ExistsResponseData existsResponseData) {

        super(false);

        infoRequestDetail = new InfoRequestDetail(existsResponseData);

        existsInfoDetail = new RestInfoDetailVer4(existsResponseData);

        this.client = ClientVer4.fromPayerXml(existsResponseData.getPayer());
    }
}
