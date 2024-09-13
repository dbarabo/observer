package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl11IndividualEntrepreneur {

    @XStreamAlias("regFact_0")
    private final StringElement regFact0; //11.1. Признак индивидуального предпринимателя = 0

    @XStreamAlias("regFact_1")
    private final StringElement regFact1; //11.1. Признак индивидуального предпринимателя = 1

    @XStreamAlias("regNum")
    private final StringElement regNum; //11.2. Регистрационный номер

    @XStreamAlias("date")
    private final StringElement date; //11.3. Дата регистрации индивидуального предпринимателя

    public Fl11IndividualEntrepreneur() {
        this.regFact0 = new StringElement("");
        this.regFact1 = null;
        this.regNum = null;
        this.date = null;
    }

    public Fl11IndividualEntrepreneur(String regNum, Date date) {
        this.regFact0 = null;
        this.regFact1 = new StringElement("");
        this.regNum = new StringElement(regNum);
        this.date = new StringElement(XmlLoader.formatDate(date));
    }
}
