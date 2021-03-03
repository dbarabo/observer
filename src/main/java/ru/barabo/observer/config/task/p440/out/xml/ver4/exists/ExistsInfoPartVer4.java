package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.out.xml.ver4.ClientVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.GeneralInfoPartVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoRequestDetail;
import ru.barabo.observer.config.task.p440.out.xml.ver4.RestInfoDetailVer4;

@XStreamAlias("СПРБННАЛИЧ")
public class ExistsInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("СведЗапр")
    private InfoRequestDetail infoRequestDetail;

    @XStreamAlias("СведСчет")
    private RestInfoDetailVer4 existsInfoDetail;

    public ExistsInfoPartVer4(ExistsResponseData existsResponseData) {

        super(false, ClientVer4.fromPayerXml(existsResponseData.getPayer()));

        infoRequestDetail = new InfoRequestDetail(existsResponseData);

        existsInfoDetail = new RestInfoDetailVer4(existsResponseData);
    }
}
