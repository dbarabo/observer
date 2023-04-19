package ru.barabo.observer.config.task.fz263.load.xml.upo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionSuspend;

import java.sql.Date;

public class Suspension extends DecisionSuspend {

    @XStreamAlias("НомУвед")
    private String numberUpo;

    @XStreamAlias("ДатаУвед")
    private String dateUpo;

    @XStreamAlias("УИД")
    private String uid;


    @Override
    public Date getDateDecision() {
        return XmlLoader.parseDate(dateUpo);
    }

    @Override
    public String getNumberDecision() {
        return numberUpo;
    }

    @Override
    public String getUID() {
        return uid;
    }
}
