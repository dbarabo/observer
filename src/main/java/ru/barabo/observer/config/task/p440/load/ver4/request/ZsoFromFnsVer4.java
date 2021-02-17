package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
public class ZsoFromFnsVer4  extends AbstractFromFns {

    @XStreamAlias("ЗАПНООСТАТ")
    private RestRequestVer4 restRequestVer4;

    @Override
    protected FromFnsInfo getFromFnsInfo() {
        return restRequestVer4;
    }

    static public ZsoFromFnsVer4 emptyZsoFromFns(String typeFormat) {
        ZsoFromFnsVer4 zsoFromFnsVer4 = new ZsoFromFnsVer4();

        zsoFromFnsVer4.setTypeInfo(typeFormat == null ? "ЗАПНООСТАТ" : typeFormat);

        zsoFromFnsVer4.restRequestVer4 = new RestRequestVer4();

        zsoFromFnsVer4.restRequestVer4.setFns(new FnsXml());

        zsoFromFnsVer4.restRequestVer4.setBank( new BankXml(null, null));

        return zsoFromFnsVer4;
    }

    static public ZsoFromFnsVer4 emptyZsoFromFns() {
        return emptyZsoFromFns(null);
    }

    static public ZsoFromFnsVer4 emptyZsoFromFnsErrorTypeFormat(String typeFormat) {
        return emptyZsoFromFns(typeFormat);
    }

}

