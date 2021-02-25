package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class GeneralInfoPartVer4 {

    @XStreamAlias("НомСправ")
    final private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

    @XStreamAlias("ДатаСправ")
    final private String dateCreate = XmlLoader.formatDate(new Date());

    @XStreamAlias("ТипСправ")
    final private String typeInquiry = "1";

    @XStreamAlias("СведБанкПБР")
    private InfoBankPBRVer4 bank;

    @XStreamAlias("КлиентБанк")
    private ClientVer4 client;
}
