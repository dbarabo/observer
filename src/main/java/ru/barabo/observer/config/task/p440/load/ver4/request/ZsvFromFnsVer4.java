package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
public class ZsvFromFnsVer4 extends AbstractFromFns {

    @XStreamAlias("ЗАПНОВЫПИС")
    private ExtractRequestVer4 extractRequestVer4;

    @Override
    protected FromFnsInfo getFromFnsInfo() {

        return extractRequestVer4;
    }

    static public ZsvFromFnsVer4 emptyZsvFromFns() {
        ZsvFromFnsVer4 zsvFromFns = new ZsvFromFnsVer4();

        zsvFromFns.setTypeInfo("ЗАПНОВЫПИС");

        zsvFromFns.extractRequestVer4 = new ExtractRequestVer4();

        zsvFromFns.extractRequestVer4.setFns(new FnsXml());

        zsvFromFns.extractRequestVer4.setBank(new BankXml(null, null) );

        return zsvFromFns;
    }

}