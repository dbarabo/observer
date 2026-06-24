package ru.barabo.observer.config.task.fz263.load.xml.upv;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
public class UpvFromFns extends AbstractFromFns {

    @XStreamAlias("ПОРПРОДВАЛ")
    private SaleCurrency saleCurrency;

    @Override
    protected FromFnsInfo getFromFnsInfo() {

        return saleCurrency;
    }

    static public UpvFromFns emptyUpvFromFns() {
        UpvFromFns upvFromFns = new UpvFromFns();

        upvFromFns.saleCurrency = new SaleCurrency();

        upvFromFns.saleCurrency.setFns(new FnsXml());

        upvFromFns.setTypeInfo("ПОРПРОДВАЛ");

        return upvFromFns;
    }
}