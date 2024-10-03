package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("FL_6_Tax")
public class Fl6Tax {

    @XStreamAlias("TaxNum_group_FL_6_Tax")
    private final TaxNumGroup taxNumGroup;

    @XStreamAlias("regNum")
    private final StringElement regNum;

    @XStreamAlias("specialMode_0")
    private final StringElement specialMode0;

    @XStreamAlias("specialMode_1")
    private final StringElement specialMode1;

    public Fl6Tax(String inn, String ogrn, Boolean isSpecialMode) {

        taxNumGroup = inn == null ? null : new TaxNumGroup(inn);

        regNum = ogrn == null ? null : new StringElement(ogrn);

        if(isSpecialMode) {
            specialMode0 = null;

            specialMode1 = new StringElement("");
        } else {
            specialMode0 = new StringElement("");

            specialMode1 = null;
        }
    }
}
