package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl29_1DebtBurdenInfo;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl55Application;

import java.util.Date;

public class FlEvent1_2 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode; // A B

    @XStreamAlias("FL_29_1_DebtBurdenInfo")
    final Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo; // minOccurs="0" Блок 29(1). Сведения о долговой нагрузке заемщика

    @XStreamAlias("FL_55_Application")
    final Fl55Application fl55Application;

    public FlEvent1_2(Integer orderNum, Date eventDate, String operationCode,
                      Fl55Application fl55Application, Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo) {

        super(orderNum, eventDate, "1.2");

        this.operationCode = operationCode;

        this.fl55Application = fl55Application;

        this.fl29_1DebtBurdenInfo = fl29_1DebtBurdenInfo;
    }

    @Override
    public String getUnicalId() {
        return fl55Application.getUid().value;
    }

    public Fl55Application getFl55Application() {
        return fl55Application;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.2";
    }
}
