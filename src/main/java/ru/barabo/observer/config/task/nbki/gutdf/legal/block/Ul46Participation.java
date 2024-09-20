package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 46. Сведения об участии в обязательстве, по которому формируется кредитная история
 */
public class Ul46Participation {

    @XStreamAlias("role")
    private final StringElement role; //minOccurs="0" 56.1. Код вида участия в сделке

    @XStreamAlias("kindCode")
    private final StringElement kindCode; //minOccurs="0"56.2. Код вида займа (кредита)

    @XStreamAlias("uid")
    private final StringElement uid; //minOccurs="0" 56.3. УИд сделки

    @XStreamAlias("fundDate")
    private final StringElement fundDate; //minOccurs="0" 56.4. Дата передачи финансирования субъекту или возникновения обеспечения исполнения обязательства

    @XStreamAlias("overdueExist_0")
    private final StringElement overdueExist0; //56.5. Признак просрочки должника более 90 дней = 0

    @XStreamAlias("overdueExist_1")
    private final StringElement overdueExist1; //56.5. Признак просрочки должника более 90 дней = 1

    @XStreamAlias("stopExist_0")
    private final StringElement stopExist0; //56.6. Признак прекращения обязательства = 0

    @XStreamAlias("stopExist_1")
    private final StringElement stopExist1; //56.6. Признак прекращения обязательства = 1

    public Ul46Participation(Integer role, Integer kindCode, String uid, Date fundDate, Boolean isOverdue90,
                             Boolean isStop) {

        this.role = new StringElement(role.toString());
        this.kindCode = new StringElement(kindCode.toString());
        this.uid = new StringElement(uid);
        this.fundDate = fundDate == null ? null : new StringElement(XmlLoader.formatDate(fundDate));
        this.overdueExist0 = isOverdue90 ? null : new StringElement("");
        this.overdueExist1 = isOverdue90 ? new StringElement("") : null;
        this.stopExist0 = isStop ? null : new StringElement("");
        this.stopExist1 = isStop ? new StringElement("") : null;
    }
}
