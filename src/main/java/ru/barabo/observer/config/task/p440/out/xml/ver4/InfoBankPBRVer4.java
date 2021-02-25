package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

@XStreamAlias("СведБанкПБР")
public class InfoBankPBRVer4 {

    @XStreamAlias("СведБанк")
    private BankXml ourBank = BankXml.ourBank();
}
