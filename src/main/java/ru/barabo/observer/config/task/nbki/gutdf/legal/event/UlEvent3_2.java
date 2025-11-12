package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul10DealUid;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class UlEvent3_2 extends AbstractEventData {

    @XStreamAlias("operationCode")
    private final String operationCode = "C.1";

    @XStreamAlias("startDate")
    private final String startDate;

    @XStreamAlias("changeReason")
    private final String changeReason = "1";

    @XStreamAlias("UL_10_DealUid")
    private final Ul10DealUid ul10DealUid;

    @XStreamAlias("UL_Event_1_4")
    private UlEvent1_4 ulEvent14;

    @XStreamAlias("UL_Event_2_1")
    private UlEvent2_1 ulEvent21;

    @XStreamAlias("UL_Event_2_2")
    private UlEvent2_2 ulEvent22;

    @XStreamAlias("UL_Event_2_3")
    private UlEvent2_3 ulEvent23;

    @XStreamAlias("UL_Event_2_4")
    private UlEvent2_4 ulEvent24;

    @XStreamAlias("UL_Event_2_5")
    private UlEvent2_5 ulEvent25;

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent1_4 ulEvent14) {

        super(orderNum, eventDate, "3.2" );

        this.startDate = XmlLoader.formatDateTimeClearTime(eventDate);

        this.ul10DealUid = ul10DealUid;

        this.ulEvent14 = ulEvent14;
    }

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent2_1 ulEvent21) {

        this(orderNum, eventDate, ul10DealUid, (UlEvent1_4) null);

        this.ulEvent21 = ulEvent21;
    }

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent2_2 ulEvent22) {

        this(orderNum, eventDate, ul10DealUid, (UlEvent2_1) null);

        this.ulEvent22 = ulEvent22;
    }

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent2_3 ulEvent23) {

        this(orderNum, eventDate, ul10DealUid, (UlEvent2_2) null);

        this.ulEvent23 = ulEvent23;
    }

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent2_4 ulEvent24) {

        this(orderNum, eventDate, ul10DealUid, (UlEvent2_3) null);

        this.ulEvent24 = ulEvent24;
    }

    public UlEvent3_2(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, UlEvent2_5 ulEvent25) {

        this(orderNum, eventDate, ul10DealUid, (UlEvent2_4) null);

        this.ulEvent25 = ulEvent25;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "3.2";
    }

    @Override
    public String getUnicalId() {
        return ul10DealUid.getUid().value;
    }

    public UlEvent1_4 getUlEvent14() {
        return ulEvent14;
    }

    public UlEvent2_1 getUlEvent21() {
        return ulEvent21;
    }

    public UlEvent2_2 getUlEvent22() {
        return ulEvent22;
    }

    public UlEvent2_3 getUlEvent23() {
        return ulEvent23;
    }

    public UlEvent2_4 getUlEvent24() {
        return ulEvent24;
    }

    public UlEvent2_5 getUlEvent25() {
        return ulEvent25;
    }
}
