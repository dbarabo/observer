package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul45Application;

import java.util.Date;

/**
 * Субъект обратился к источнику с предложением совершить сделку
 */
public class UlEvent1_1 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "A";

    @XStreamAlias("UL_45_Application")
    final Ul45Application ul45Application;

    public UlEvent1_1(Integer orderNum, Date eventDate, Ul45Application ul45Application) {
        super(orderNum, eventDate, "1.1");

        this.ul45Application = ul45Application;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.1";
    }

    @Override
    public String getUnicalId() {
        return ul45Application.getUid().value;
    }

    public Ul45Application getUl45Application() {
        return ul45Application;
    }
}
