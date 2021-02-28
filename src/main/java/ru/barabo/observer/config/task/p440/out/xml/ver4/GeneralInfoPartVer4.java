package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class GeneralInfoPartVer4 {

    @XStreamAlias("НомСправ")
    private String numberHelp;

    @XStreamAlias("НомВыпис")
    private String numberExtract;

    @XStreamAlias("ДатаСправ")
    private String dateHelp;

    @XStreamAlias("ДатаВыпис")
    private String dateExtract;

    @XStreamAlias("ТипСправ")
    final private String typeInquiry = "1";

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

    @XStreamAlias("КлиентБанк")
    private ClientVer4 client;

    protected GeneralInfoPartVer4(boolean isExtract, ClientVer4 client) {
        if(isExtract) {
            numberExtract = (System.currentTimeMillis() / 100) % 10000000 + "";

            dateExtract = XmlLoader.formatDate(new Date());
        } else {
            numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

            dateHelp = XmlLoader.formatDate(new Date());
        }

        this.client = client;
    }
}
