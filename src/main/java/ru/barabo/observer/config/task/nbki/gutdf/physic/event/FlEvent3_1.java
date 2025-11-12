package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl17DealUid;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.current.Fl3235GroupCurrentNew;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.current.Fl55_57GroupCurrentNew;

import java.util.Date;
import java.util.List;

public class FlEvent3_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "C.1";

    @XStreamAlias("changeReason")
    private final String changeReason = "1";

    @XStreamAlias("FL_55_57_Group")
    private Fl55_57GroupCurrentNew fl55_57Group;

    @XStreamAlias("FL_17_DealUid")
    private Fl17DealUid fl17DealUid;

    @XStreamImplicit(itemFieldName = "FL_32_35_Group")
    private List<Fl3235GroupCurrentNew> fl3235GroupCurrentNewList;

    public FlEvent3_1(Integer orderNum, Date eventDate, Fl55_57GroupCurrentNew fl55_57Group) {
        super(orderNum, eventDate, "3.1");

        this.fl55_57Group = fl55_57Group;
    }

    public FlEvent3_1(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, List<Fl3235GroupCurrentNew> fl3235GroupCurrentNewList) {
        super(orderNum, eventDate, "3.1");

        this.fl17DealUid = fl17DealUid;

        this.fl3235GroupCurrentNewList = fl3235GroupCurrentNewList;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "3.1";
    }

    @Override
    public String getUnicalId() {

        return (fl17DealUid != null)
                ? fl17DealUid.getUid().value
                : fl55_57Group.getFl55_57GroupNew().getFl55Application().getUid().value;
    }
}
