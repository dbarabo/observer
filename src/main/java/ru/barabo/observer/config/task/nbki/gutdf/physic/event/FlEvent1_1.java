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
        super(orderNum, eventDate);

        this.fl55Application = fl55Application;
    }
}
