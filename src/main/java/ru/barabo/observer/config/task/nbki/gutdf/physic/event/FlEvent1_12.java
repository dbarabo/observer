package ru.barabo.observer.config.task.nbki.gutdf.physic.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl13Bankruptcy;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl14BankruptcyEnd;

import java.util.Date;

/**
 * Изменились сведения по делу о банкротстве субъекта
 */
public class FlEvent1_12 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("FL_13_Bankruptcy")
    final Fl13Bankruptcy fl13Bankruptcy; // Блок 13. Сведения по делу о несостоятельности (банкротстве)

    @XStreamAlias("FL_14_BankruptcyEnd")
    final Fl14BankruptcyEnd fl14BankruptcyEnd; // Блок 14. Сведения о завершении расчетов с кредиторами и освобождении субъекта от исполнения обязательств в связи с банкротством

    public FlEvent1_12(Integer orderNum, Date eventDate, Fl13Bankruptcy fl13Bankruptcy, Fl14BankruptcyEnd fl14BankruptcyEnd) {
        super(orderNum, eventDate);

        this.fl13Bankruptcy = fl13Bankruptcy;

        this.fl14BankruptcyEnd = fl14BankruptcyEnd;
    }

    @Override
    public String getEvent() {
        return "1.12";
    }

    @Override
    public String getUnicalId() {
        return null;
    }
}
