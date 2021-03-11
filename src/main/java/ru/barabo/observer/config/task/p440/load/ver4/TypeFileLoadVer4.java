package ru.barabo.observer.config.task.p440.load.ver4;

import ru.barabo.observer.config.task.p440.load.ver4.request.ZsoFromFnsVer4;
import ru.barabo.observer.config.task.p440.load.ver4.request.ZsvFromFnsVer4;
import ru.barabo.observer.config.task.p440.load.xml.apx.ApnFromFns;
import ru.barabo.observer.config.task.p440.load.xml.apx.ApoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.apx.ApzFromFns;
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns;
import ru.barabo.observer.config.task.p440.load.xml.decision.RpoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.pno.PnoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.request.ZsnFromFns;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.IzvFromFns;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtFromFns;

public enum TypeFileLoadVer4 {

    RPO(RpoFromFns.class, "RPO"),

    ROO(RooFromFns.class, "ROO"),

    PNO(PnoFromFns.class, "PNO"),

    ZSN(ZsnFromFns.class, "ZSN"),

    ZSO(ZsoFromFnsVer4.class, "ZSO"),

    ZSV(ZsvFromFnsVer4.class, "ZSV"),

    IZV(IzvFromFns.class, "IZV"),

    APN(ApnFromFns.class, "APN"),

    APZ(ApzFromFns.class, "APZ"),

    KWT(KwtFromFns.class, "KWT"),

    APO(ApoFromFns.class, "APO");

    private Class clazz;

    private final String prefixFile;

    TypeFileLoadVer4(Class clazz, String prefixFile) {
        this.clazz = clazz;

        this.prefixFile = prefixFile;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getPrefixFile() {
        return prefixFile;
    }
}
