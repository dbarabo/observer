package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 27(1). Сведения о погашении требований кредитора по обязательству предоставлением отступного, зачетом встречных
 * требований, прощением долга, за счет реализации предмета лизинга
 */
public class Ul271ProvisionPaymentOffset {
    @XStreamAlias("exist_0")
    private final StringElement exist0 = new StringElement(""); // 27(1).1. Признак погашения требований кредитора по обязательству предоставлением отступного, зачетом встречных требований, прощением долга, за счет реализации предмета лизинга = 0
}
