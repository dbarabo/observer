package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

/**
 * Блок 1. Наименование юридического лица
 */
public class Ul1Name {

    @XStreamAlias("fullName")
    private final StringElement fullName; // emptyValueType cyrilicStringType 1.1. Полное наименование

    @XStreamAlias("shortName")
    private final StringElement shortName; // emptyValueType cyrilicStringType  1.2. Сокращенное наименование

    @XStreamAlias("otherName")
    private final StringElement otherName; // minOccurs="0" 1.3. Иное наименование

    @XStreamAlias("change_0")
    private final StringElement change0; // 1.4. Признак смены наименования = 0

    @XStreamAlias("change_1")
    private final StringElement change1; // 1.4. Признак смены наименования = 1

    @XStreamAlias("prevFull")
    private final StringElement prevFull; // 1.5. Полное наименование до его смены


    public Ul1Name(String fullName, String shortName, String otherName, String prevFull) {

        this.fullName = new StringElement(fullName);

        this.shortName = new StringElement(shortName);

        this.otherName = otherName == null ? null :  new StringElement(otherName);

        if(prevFull == null) {

            this.change0 = new StringElement("");

            this.change1 = null;

            this.prevFull = null;

        } else {
            this.change0 = null;

            this.change1 = new StringElement("");

            this.prevFull = new StringElement(prevFull);
        }
    }
}
