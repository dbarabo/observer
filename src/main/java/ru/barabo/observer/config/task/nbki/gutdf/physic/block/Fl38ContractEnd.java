package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 38. Сведения о прекращении обязательства
 */
public class Fl38ContractEnd {

    @XStreamAlias("date")
    private final StringElement date; // 38.2. Дата фактического прекращения обязательства

    @XStreamAlias("code")
    private final StringElement code; // 38.1. Код основания прекращения обязательства

    public Fl38ContractEnd(Date date, Integer code) {

        this.date = new StringElement(XmlLoader.formatDate(date));

        this.code = new StringElement(code.toString());
    }
}
