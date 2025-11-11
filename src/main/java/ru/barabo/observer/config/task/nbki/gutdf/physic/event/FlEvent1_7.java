package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.Ul1Name;
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.*;

import java.util.Date;

/**
 * Изменились сведения титульной части кредитной истории субъекта
 */
public class FlEvent1_7 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_1_4_Group")
    private final Fl1_4Group fl14Group;

    @XStreamAlias("FL_2_5_Group")
    private final Fl2_5Group fl25Group;

    @XStreamAlias("FL_3_Birth")
    private final Fl3Birth fl3Birth;

    @XStreamAlias("FL_6_Tax")
    private final Fl6Tax fl6Tax; // minOccurs="0"

    @XStreamAlias("FL_7_Social")
    private final Fl7Social fl7Social;  // minOccurs="0"

    public FlEvent1_7(Integer orderNum, Date eventDate, Fl1_4Group fl14Group, Fl2_5Group fl25Group, Fl3Birth fl3Birth,
                      Fl6Tax fl6Tax, Fl7Social fl7Social) {
        super(orderNum, eventDate, "1.7");

        this.fl14Group = fl14Group;

        this.fl25Group = fl25Group;

        this.fl3Birth = fl3Birth;

        this.fl6Tax = fl6Tax;

        this.fl7Social = fl7Social;
    }

    @Override
    public String getUnicalId() {
        return null;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.7";
    }

    public Fl1_4Group getFl14Group() {
        return fl14Group;
    }

    public Fl2_5Group getFl25Group() {
        return fl25Group;
    }

    public Fl3Birth getFl3Birth() {
        return fl3Birth;
    }

    public Fl6Tax getFl6Tax() {
        return fl6Tax;
    }

    public Fl7Social getFl7Social() {
        return fl7Social;
    }
}
