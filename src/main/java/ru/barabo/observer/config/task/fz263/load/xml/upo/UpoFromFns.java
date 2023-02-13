package ru.barabo.observer.config.task.fz263.load.xml.upo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class UpoFromFns extends AbstractFromFns {

    @XStreamAlias("УВЕДОПРИОС")
    private Suspension suspension;

    @Override
    protected FromFnsInfo getFromFnsInfo() {

        return suspension;
    }

    static public UpoFromFns emptyUpoFromFns() {
        UpoFromFns upoFromFns = new UpoFromFns();

        upoFromFns.suspension = new Suspension();

        upoFromFns.suspension.setFns(new FnsXml());

        upoFromFns.setTypeInfo("УВЕДОПРИОС");

        return upoFromFns;
    }
}
