package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl17DealUid;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class FlEvent3_2 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "C.1";

    @XStreamAlias("startDate")
    final String startDate;

    @XStreamAlias("FL_17_DealUid")
    final Fl17DealUid fl17DealUid;

    @XStreamAlias("FL_Event_1_4")
    final FlEvent1_4 flEvent14;

    @XStreamAlias("FL_Event_2_1")
    FlEvent2_1 flEvent21;

    @XStreamAlias("FL_Event_2_2")
    FlEvent2_2 flEvent22;

    @XStreamAlias("FL_Event_2_3")
    FlEvent2_3 flEvent23;

    public FlEvent3_2(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, FlEvent1_4 flEvent14) {

        super(orderNum, eventDate, "3.2" );

        this.startDate = XmlLoader.formatDate(eventDate);

        this.fl17DealUid = fl17DealUid;

        this.flEvent14 = flEvent14;
    }

    public FlEvent3_2(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, FlEvent2_2 flEvent22) {

        this(orderNum, eventDate, fl17DealUid, (FlEvent1_4) null);

        this.flEvent22 = flEvent22;
    }

    public FlEvent3_2(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, FlEvent2_3 flEvent23) {

        this(orderNum, eventDate, fl17DealUid, (FlEvent2_2) null);

        this.flEvent23 = flEvent23;
    }

    public FlEvent3_2(Integer orderNum, Date eventDate, Fl17DealUid fl17DealUid, FlEvent2_1 flEvent21) {

        this(orderNum, eventDate, fl17DealUid, (FlEvent2_3) null);

        this.flEvent21 = flEvent21;
    }


    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "3.2";
    }

    @Override
    public String getUnicalId() {
        return fl17DealUid.getUid().value;
    }

    public FlEvent1_4 getFlEvent14() {
        return flEvent14;
    }

    public FlEvent2_2 getFlEvent22() {
        return flEvent22;
    }

    public FlEvent2_3 getFlEvent23() {
        return flEvent23;
    }

    public FlEvent2_1 getFlEvent21() {
        return flEvent21;
    }
}
