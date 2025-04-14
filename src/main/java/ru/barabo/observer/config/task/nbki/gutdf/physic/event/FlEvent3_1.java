package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.current.Fl55_57GroupCurrentNew;

import java.util.Date;

public class FlEvent3_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "C.1";

    @XStreamAlias("FL_55_57_Group")
    final Fl55_57GroupCurrentNew fl55_57Group;

    public FlEvent3_1(Integer orderNum, Date eventDate, Fl55_57GroupCurrentNew fl55_57Group) {
        super(orderNum, eventDate, "3.1");

        this.fl55_57Group = fl55_57Group;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "3.1";
    }

    @Override
    public String getUnicalId() {
        return fl55_57Group.getFl55_57GroupNew().getFl55Application().getUid().value;
    }
}
