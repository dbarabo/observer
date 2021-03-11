package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseDataVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.ClientVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.GeneralInfoPartVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoBankPBRVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoRequestDetail;

@XStreamAlias("СПРБННАЛИЧ")
public class ExistsInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("СведЗапр")
    final private InfoRequestDetail infoRequestDetail;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

    @XStreamAlias("КлиентБанк")
    final private ClientVer4 client;

    @XStreamAlias("СведСчет")
    final private ExistsInfoDetailVer4 existsInfoDetail;

    public ExistsInfoPartVer4(ExistsResponseDataVer4 existsResponseData) {

        super(false);

        infoRequestDetail = new InfoRequestDetail(existsResponseData);

        existsInfoDetail = new ExistsInfoDetailVer4(existsResponseData);

        this.client = ClientVer4.fromPayerXml(existsResponseData.getPayer());
    }
}
