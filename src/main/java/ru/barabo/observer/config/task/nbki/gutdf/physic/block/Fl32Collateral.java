package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 32. Сведения о залоге
 */
public class Fl32Collateral {

    @XStreamAlias("code")
    private final StringElement code; // 32.2. Код предмета залога

    // TODO

    public Fl32Collateral(String code) {

        this.code = new StringElement(code);
    }
}
