package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;

import java.util.Date;

/**
 * Изменились сведения титульной части кредитной истории субъекта
 */
public class UlEvent1_7 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    public UlEvent1_7(Integer orderNum, Date eventDate) {
        super(orderNum, eventDate);
    }

    @Override
    public String getEvent() {
        return "1.7";
    }

    @Override
    public String getUnicalId() {
        return null;
    }
}
