package ru.barabo.observer.config.task.fz263.load.xml.uno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("Файл")
public class UnoFromFns extends AbstractFromFns {

    @XStreamAlias("ПОРУЧСЧЕТН")
    private OrderTaxInfo263fz orderTaxInfo263fz;

    @Override
    public FromFnsInfo getFromFnsInfo() {

        return orderTaxInfo263fz;
    }

    static public UnoFromFns emptyFromFns() {
        UnoFromFns unoFromFns = new UnoFromFns();

        unoFromFns.orderTaxInfo263fz = new OrderTaxInfo263fz();

        unoFromFns.setTypeInfo("ПОРУЧСЧЕТН");

        return unoFromFns;
    }
}
