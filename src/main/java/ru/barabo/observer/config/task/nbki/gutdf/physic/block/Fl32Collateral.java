package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;
import java.util.List;

/**
 * Блок 32. Сведения о залоге
 */
public class Fl32Collateral {

    @XStreamAlias("code")
    private final StringElement code; // 32.2. Код предмета залога

    @XStreamAlias("date")
    private final StringElement date; // emptyValueType| 32.4. Дата заключения договора залога

    @XStreamImplicit(itemFieldName = "Sum_group_FL_32_35_Group")
    private final List<SumGroupFl32_35Group> sumGroupFl32_35GroupList; // minOccurs="0"  Предметы залога

    @XStreamAlias("collateralEndDate")
    private final StringElement collateralEndDate; // minOccurs="0 32.9, Дата прекращения залога согласно договору

    @XStreamAlias("collateralFactEndDate")
    private final StringElement collateralFactEndDate; // minOccurs="0 32.10. Дата фактического прекращения залога

    @XStreamAlias("endReason")
    private final StringElement endReason; // minOccurs="0  32.11. Код причины прекращения залога

    @XStreamAlias("contractTotalSum")
    private final StringElement contractTotalSum; // dec15p2Type 32.13. Сумма обязательств по всем договорам, исполнение которых обеспечено предметом залога

    @XStreamAlias("contractCount")
    private final StringElement contractCount; // 32.14. Количество договоров, исполнение обязательств по которым обеспечено предметом залога

    @XStreamAlias("location")
    private final StringElement okato; // minOccurs="0" 32.15. Код местонахождения залога по ОКАТО

    @XStreamAlias("actualCost")
    private final StringElement actualCost; // dec15p2Type minOccurs="0 32.16. Актуальная стоимость предмета залога

    @XStreamAlias("calcDate")
    private final StringElement calcDate; // minOccurs="0 32.17. Дата расчета актуальной стоимости предмета залога

    public Fl32Collateral(String code, Date date, List<SumGroupFl32_35Group> sumGroupFl3235GroupList,
                          Date collateralEndDate, Date collateralFactEndDate, Integer endReason,
                          Number contractTotalSum, Integer contractCount, String okato, Number actualCost,
                          Date calcDate) {

        this.code = new StringElement(code);

        this.date = new StringElement(XmlLoader.formatDate(date));

        sumGroupFl32_35GroupList = sumGroupFl3235GroupList;

        this.collateralEndDate = collateralEndDate == null ? null : new StringElement(XmlLoader.formatDate(collateralEndDate));

        this.collateralFactEndDate = collateralFactEndDate == null ? null : new StringElement(XmlLoader.formatDate(collateralFactEndDate));

        this.endReason = endReason == null ? null : new StringElement(endReason.toString());

        this.contractTotalSum = new StringElement(XmlLoader.formatSum(contractTotalSum));

        this.contractCount = new StringElement(contractCount.toString());

        this.okato = okato == null ? null : new StringElement(okato);

        this.actualCost = actualCost == null ? null : new StringElement(XmlLoader.formatSum(actualCost));

        this.calcDate = actualCost == null ? null : new StringElement(XmlLoader.formatDate(calcDate));
    }
}
