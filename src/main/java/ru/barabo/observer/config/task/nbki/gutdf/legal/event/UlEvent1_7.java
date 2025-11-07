package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.Ul1Name;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.Ul2Address;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.Ul3RegType;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.Ul4TaxType;

import java.util.Date;

/**
 * Изменились сведения титульной части кредитной истории субъекта
 */
public class UlEvent1_7 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_1_Name")
    private final Ul1Name ul1Name; // Блок 1. Наименование юридического лица

    @XStreamAlias("UL_2_Address")
    private final Ul2Address ul2Address;  // Блок 2. Адрес юридического лица в пределах его места нахождения и контактная информация

    @XStreamAlias("UL_3_Reg")
    private final Ul3RegType ul3Reg; // minOccurs="0" Блок 3. Регистрационный номер

    @XStreamAlias("UL_4_Tax")
    private final Ul4TaxType ul4Tax;  // minOccurs="0" Блок 4. Номер налогоплательщика

    public UlEvent1_7(Integer orderNum, Date eventDate, Ul1Name ul1Name, Ul2Address ul2Address,
                      Ul3RegType ul3Reg, Ul4TaxType ul4Tax) {
        super(orderNum, eventDate, "1.7");

        this.ul1Name = ul1Name;

        this.ul2Address = ul2Address;

        this.ul3Reg = ul3Reg;

        this.ul4Tax = ul4Tax;
    }

    @Override
    public String getEvent() {
        return super.getEvent() != null ? super.getEvent() : "1.7";
    }

    @Override
    public String getUnicalId() {
        return null;
    }

    public Ul1Name getUl1Name() {
        return ul1Name;
    }

    public Ul2Address getUl2Address() {
        return ul2Address;
    }

    public Ul3RegType getUl3Reg() {
        return ul3Reg;
    }

    public Ul4TaxType getUl4Tax() {
        return ul4Tax;
    }
}
