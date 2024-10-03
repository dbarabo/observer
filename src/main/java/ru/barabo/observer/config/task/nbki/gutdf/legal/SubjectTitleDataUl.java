package ru.barabo.observer.config.task.nbki.gutdf.legal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.*;

import java.util.List;

/**
 * Титульная часть
 */
public class SubjectTitleDataUl {

    @XStreamAlias("UL_1_Name")
    private final Ul1Name ul1Name; // Блок 1. Наименование юридического лица

    @XStreamAlias("UL_2_Address")
    private final Ul2Address ul2Address;  // Блок 2. Адрес юридического лица в пределах его места нахождения и контактная информация

    @XStreamAlias("UL_3_Reg")
    private final Ul3RegType ul3Reg; // minOccurs="0" Блок 3. Регистрационный номер

    @XStreamAlias("UL_4_Tax")
    private final Ul4TaxType ul4Tax;  // minOccurs="0" Блок 4. Номер налогоплательщика

    @XStreamImplicit(itemFieldName = "UL_5_Reorg")
    private final List<Ul5Reorg> ul5ReorgList; // minOccurs="0" Блок 5. Сведения о смене наименования либо правопреемстве при реорганизации

    public SubjectTitleDataUl(Ul1Name ul1Name, Ul2Address ul2Address,
                              Ul3RegType ul3Reg, Ul4TaxType ul4Tax, List<Ul5Reorg> ul5ReorgList) {
        this.ul1Name = ul1Name;
        this.ul2Address = ul2Address;
        this.ul3Reg = ul3Reg;
        this.ul4Tax = ul4Tax;
        this.ul5ReorgList = ul5ReorgList;
    }
}
