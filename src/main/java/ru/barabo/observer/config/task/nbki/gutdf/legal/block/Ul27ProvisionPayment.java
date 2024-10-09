package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * >Блок 27. Сведения о погашении требований кредитора по обязательству за счет обеспечения
 */
public class Ul27ProvisionPayment {
    @XStreamAlias("exist_0")
    private final StringElement exist0 = new StringElement(""); // 36.1. Признак погашения требований за счет обеспечения = 0
}
