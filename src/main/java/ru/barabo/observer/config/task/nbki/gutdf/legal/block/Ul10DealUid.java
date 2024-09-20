package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 10. Идентификатор сделки
 */
public class Ul10DealUid {

    @XStreamAlias("uid")
    private final StringElement uid; //10.1. УИд сделки

    @XStreamAlias("num")
    private final StringElement num; //minOccurs="0" 10.2. Номер сделки

    @XStreamAlias("refUid")
    private final StringElement refUid; //minOccurs="0" 10.3. УИд сделки, по которой задолженность была полностью или частично рефинансирована

    @XStreamAlias("openDate")
    private final StringElement openDate; //emptyValueType 10.4. Дата совершения сделки

    public Ul10DealUid(String uid, String num, String refUid, Date openDate) {

        this.uid = new StringElement(uid);

        this.num = num == null ? null : new StringElement(num);

        this.refUid = num == null ? null : new StringElement(refUid);

        this.openDate = new StringElement(openDate == null ? "-" : XmlLoader.formatDate(openDate));
    }
}
