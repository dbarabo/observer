package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul45Application;

import java.util.Date;

/**
 * Источник одобрил обращение (направил ему оферту) или изменились сведения об обращении
 */
public class UlEvent1_2 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode; // A B

    @XStreamAlias("UL_45_Application")
    final Ul45Application ul45Application;

    public UlEvent1_2(Integer orderNum, Date eventDate, String operationCode,
                      Ul45Application ul45Application) {
        super(orderNum, eventDate, "1.2");

        this.operationCode = operationCode;

        this.ul45Application = ul45Application;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.2";
    }

    @Override
    public String getUnicalId() {
        return ul45Application.getUid().value;
    }

    public Ul45Application getUl45Application() {
        return ul45Application;
    }
}
