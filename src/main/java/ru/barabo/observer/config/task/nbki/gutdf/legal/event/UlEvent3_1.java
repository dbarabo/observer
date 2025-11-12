package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.current.Ul45_47GroupCurrentNew;

import java.util.Date;

public class UlEvent3_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "C.1";

    @XStreamAlias("changeReason")
    private final String changeReason = "1";

    @XStreamAlias("UL_45_47_Group")
    final Ul45_47GroupCurrentNew ul45_47Group;

    public UlEvent3_1(Integer orderNum, Date eventDate, Ul45_47GroupCurrentNew ul45_47Group) {
        super(orderNum, eventDate, "3.1");

        this.ul45_47Group = ul45_47Group;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "3.1";
    }

    @Override
    public String getUnicalId() {
        return ul45_47Group.getUl45_47GroupNew().getUl45Application().getUid().value;
    }
}