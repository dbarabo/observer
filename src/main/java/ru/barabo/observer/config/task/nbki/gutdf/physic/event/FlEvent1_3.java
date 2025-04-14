package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl29_1DebtBurdenInfo;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl55Application;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl57Reject;

import java.util.Date;

@XStreamAlias("FL_event_1.3")
public class FlEvent1_3  extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_29_1_DebtBurdenInfo")
    final Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo; // minOccurs="0" Блок 29(1). Сведения о долговой нагрузке заемщика

    @XStreamAlias("FL_55_Application")
    final Fl55Application fl55Application;

    @XStreamAlias("FL_57_Reject")
    final Fl57Reject fl57Reject;

    public FlEvent1_3(Integer orderNum, Date eventDate, Fl55Application fl55Application,
                      Fl29_1DebtBurdenInfo fl29_1DebtBurdenInfo, Fl57Reject fl57Reject) {

        super(orderNum, eventDate, "1.3");

        this.fl29_1DebtBurdenInfo = fl29_1DebtBurdenInfo;

        this.fl55Application = fl55Application;

        this.fl57Reject = fl57Reject;
    }

    @Override
    public String getUnicalId() {
        return fl55Application.getUid().value;
    }

    public Fl55Application getFl55Application() {
        return fl55Application;
    }

    public Fl57Reject getFl57Reject() {
        return fl57Reject;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.3";
    }
}
