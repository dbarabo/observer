package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl55Application;

import java.util.Date;

public class FlEvent1_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "A";

    @XStreamAlias("FL_55_Application")
    final Fl55Application fl55Application;

    public FlEvent1_1(Integer orderNum, Date eventDate, Fl55Application fl55Application) {
        super(orderNum, eventDate, "1.1");

        this.fl55Application = fl55Application;
    }

    @Override
    public String getUnicalId() {
        return fl55Application.getUid().value;
    }

    public Fl55Application getFl55Application() {
        return fl55Application;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.1";
    }
}
