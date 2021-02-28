package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

@XStreamAlias("СведБанкПБР")
public class InfoBankPBRVer4 {

    private static class SingltonOurBank {
        private static final InfoBankPBRVer4 ourBankPBR = new InfoBankPBRVer4();
    }

    static public InfoBankPBRVer4 ourBank() {
        return SingltonOurBank.ourBankPBR;
    }

    @XStreamAlias("СведБанк")
    private BankXml ourBank = BankXml.ourBank();
}
