package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul45Application;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul47Reject;

import java.util.Date;

/**
 * Источник отказался от совершения сделки по обращению
 */
public class UlEvent1_3 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_45_Application")
    final Ul45Application ul45Application; // >Блок 45. Сведения об обращении субъекта к источнику с предложением совершить сделку

    @XStreamAlias("UL_47_Reject")
    final Ul47Reject ul47Reject; // Блок 47. Сведения об отказе источника от предложения совершить сделку

    public UlEvent1_3(Integer orderNum, Date eventDate, Ul45Application ul45Application, Ul47Reject ul47Reject) {
        super(orderNum, eventDate);

        this.ul45Application = ul45Application;

        this.ul47Reject = ul47Reject;
    }

    @Override
    public String getEvent() {
        return "1.1";
    }

    @Override
    public String getUnicalId() {
        return ul45Application.getUid().value;
    }

    public Ul45Application getUl45Application() {
        return ul45Application;
    }
}