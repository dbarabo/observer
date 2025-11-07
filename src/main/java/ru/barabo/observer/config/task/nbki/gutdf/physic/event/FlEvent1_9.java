package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl10Contact;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl11IndividualEntrepreneur;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl8AddrReg;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl9AddrFact;

import java.util.Date;
import java.util.List;

/**
 * Изменились сведения о субъекте в основной части кредитной истории, кроме сведений о дееспособности, банкротстве, индивидуальном рейтинге и кредитной оценке
 */
public class FlEvent1_9 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_8_AddrReg")
    final Fl8AddrReg fl8AddrReg; // Блок 8. Регистрация физического лица по месту жительства или пребывания

    @XStreamAlias("FL_9_AddrFact")
    final Fl9AddrFact fl9AddrFact; // Блок 9. Фактическое место жительства

    @XStreamImplicit(itemFieldName = "FL_10_Contact")
    final List<Fl10Contact> fl10ContactList; // Блок 10. Контактные данные

    @XStreamAlias("FL_11_IndividualEntrepreneur")
    final Fl11IndividualEntrepreneur fl11IndividualEntrepreneur; // Блок 11. Государственная регистрация в качестве индивидуального предпринимателя

    public FlEvent1_9(Integer orderNum, Date eventDate, Fl8AddrReg fl8AddrReg, Fl9AddrFact fl9AddrFact,
                      List<Fl10Contact> fl10ContactList, Fl11IndividualEntrepreneur fl11IndividualEntrepreneur) {
        super(orderNum, eventDate, "1.9");

        this.fl8AddrReg = fl8AddrReg;

        this.fl9AddrFact = fl9AddrFact;

        this.fl10ContactList = fl10ContactList;

        this.fl11IndividualEntrepreneur = fl11IndividualEntrepreneur;
    }

    @Override
    public String getUnicalId() {
        return null;
    }

    public Fl8AddrReg getFl8AddrReg() {
        return fl8AddrReg;
    }

    public Fl9AddrFact getFl9AddrFact() {
        return fl9AddrFact;
    }

    public List<Fl10Contact> getFl10ContactList() {
        return fl10ContactList;
    }

    public Fl11IndividualEntrepreneur getFl11IndividualEntrepreneur() {
        return fl11IndividualEntrepreneur;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.9";
    }
}
