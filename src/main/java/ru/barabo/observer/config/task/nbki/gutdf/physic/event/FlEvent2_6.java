package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl17DealUid;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl39Court;

import java.util.Date;

public class FlEvent2_6 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid; // Блок 17. Идентификатор сделки

    @XStreamAlias("FL_39_Court")
    final Fl39Court fl39Court; // Блок 18. Общие сведения о сделкеБлок 39. Сведения о судебном споре или требовании по обязательству


    public FlEvent2_6(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, Fl39Court fl39Court) {

        super(orderNum, eventDate);

        this.fl17DealUid = fl17DealUid;

        this.fl39Court = fl39Court;
    }

    @Override
    public String getEvent() {
        return "2.6";
    }

    @Override
    public String getUnicalId() {
        return fl17DealUid.getUid().value;
    }
}
