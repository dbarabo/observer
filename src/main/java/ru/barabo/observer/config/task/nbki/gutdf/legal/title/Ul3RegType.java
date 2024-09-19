package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 3. Регистрационный номер
 */
public class Ul3RegType {

    @XStreamAlias("regNum")
    private final StringElement ogrn; // 3.1. Регистрационный номер

    @XStreamAlias("lei")
    private final StringElement lei; // minOccurs="0" 3.2. Идентификатор LEI

    public Ul3RegType(String ogrn, String lei) {

        this.ogrn = new StringElement(ogrn);

        this.lei = lei == null ? null : new StringElement(lei);
    }
}
