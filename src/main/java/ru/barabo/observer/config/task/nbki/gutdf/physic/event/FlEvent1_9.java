package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.*;

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

    @XStreamAlias("FL_10_Contact")
    final Fl10Contact fl10Contact; // Блок 10. Контактные данные

    @XStreamAlias("FL_11_IndividualEntrepreneur")
    final Fl11IndividualEntrepreneur fl11IndividualEntrepreneur; // Блок 11. Государственная регистрация в качестве индивидуального предпринимателя

    public FlEvent1_9(Integer orderNum, Date eventDate, Fl8AddrReg fl8AddrReg, Fl9AddrFact fl9AddrFact,
                      Fl10Contact fl10Contact, Fl11IndividualEntrepreneur fl11IndividualEntrepreneur) {
        super(orderNum, eventDate);

        this.fl8AddrReg = fl8AddrReg;

        this.fl9AddrFact = fl9AddrFact;

        this.fl10Contact = fl10Contact;

        this.fl11IndividualEntrepreneur = fl11IndividualEntrepreneur;
    }
}
