package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 25. Сведения о независимой гарантии
 */
public class Ul25Guarantee {
    @XStreamAlias("exist_0")
    private final StringElement exist0 = new StringElement(""); // 34.1. Признак наличия независимой гарантии = 0
}
