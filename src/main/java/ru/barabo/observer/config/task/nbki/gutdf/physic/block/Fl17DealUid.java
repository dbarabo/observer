package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

public class Fl17DealUid {

    @XStreamAlias("uid")
    private final StringElement uid; // 17.1. УИд сделки

    @XStreamAlias("num")
    private final StringElement num; // 17.2. Номер сделки

    @XStreamAlias("refUid")
    private final StringElement refUid; // minOccurs="0" 17.3. УИд сделки, по которой задолженность была полностью или частично рефинансирована

    @XStreamAlias("openDate")
    private final StringElement openDate; // 17.4. Дата совершения сделки emptyValueType | xs:date

    public Fl17DealUid(String uid, String num, String refUid, Date openDate) {

        this.uid = new StringElement(uid);

        this.num = num == null ? null : new StringElement(num);

        this.refUid = (refUid == null || refUid.isEmpty())? null : new StringElement(refUid);

        this.openDate = new StringElement(openDate == null ? "-" : XmlLoader.formatDate(openDate));
    }

    public StringElement getUid() {
        return uid;
    }

    public StringElement getNum() {
        return num;
    }

    public StringElement getRefUid() {
        return refUid;
    }

    public StringElement getOpenDate() {
        return openDate;
    }
}
