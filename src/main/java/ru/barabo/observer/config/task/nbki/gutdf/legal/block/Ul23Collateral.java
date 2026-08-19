package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;
import java.util.List;

/**
 * Блок 23. Сведения о залоге
 */
public class Ul23Collateral {

    @XStreamAlias("code")
    private final StringElement code; // 32.2. Код предмета залога

    @XStreamAlias("date")
    private final StringElement date; // emptyValueType| 32.4. Дата заключения договора залога

    @XStreamAlias("startDate")
    private final StringElement startDate; // emptyValueType| 23.4. Дата возникновения залога

    @XStreamImplicit(itemFieldName = "Sum_group_UL_23_26_Group")
    private final List<SumGroupUl23_26Group> sumGroupUl23_26Group; // minOccurs="0"  Предметы залога

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

    @XStreamAlias("actualPriceCode")
    private final StringElement actualPriceCode; // minOccurs="0 32.17. Дата расчета актуальной стоимости предмета залога

    @XStreamAlias("acquiredExist_1")
    private final StringElement acquiredExist1; // 23.19. Признак залога приобретаемого объекта по сделке = 1

    @XStreamAlias("inhabitedExist_0")
    private final StringElement inhabitedExist0; // 23.20. Признак залога жилой недвижимости = 0

    @XStreamAlias("inhabitedExist_1")
    private final StringElement inhabitedExist1; // 23.20. Признак залога жилой недвижимости = 1

    public Ul23Collateral(String code, Date date, List<SumGroupUl23_26Group> sumGroupUl23_26Group,
                          Date collateralEndDate, Date collateralFactEndDate, Integer endReason,
                          Number contractTotalSum, Integer contractCount, String okato, Number actualCost,
                          Date calcDate, Integer priceCode, Boolean isHomePledge) {

        this.code = new StringElement(code);

        this.date = XmlLoader.isNewFormatGutdf() ? null : new StringElement(XmlLoader.formatDate(date));

        this.startDate = XmlLoader.isNewFormatGutdf() ? new StringElement(XmlLoader.formatDate(date)) : null;

        this.sumGroupUl23_26Group = sumGroupUl23_26Group;

        this.collateralEndDate = collateralEndDate == null ? null : new StringElement(XmlLoader.formatDate(collateralEndDate));

        this.collateralFactEndDate = collateralFactEndDate == null ? null : new StringElement(XmlLoader.formatDate(collateralFactEndDate));

        this.endReason = endReason == null ? null : new StringElement(endReason.toString());

        this.contractTotalSum = new StringElement(XmlLoader.formatSum(contractTotalSum));

        this.contractCount = new StringElement(contractCount.toString());

        this.okato = okato == null ? null : new StringElement(okato);

        this.actualCost = actualCost == null ? null : new StringElement(XmlLoader.formatSum(actualCost));

        this.calcDate = calcDate == null ? null : new StringElement(XmlLoader.formatDate(calcDate));

        this.actualPriceCode = (XmlLoader.isNewFormatGutdf() && (calcDate != null) ) ? new StringElement(priceCode == null ? "-" : priceCode.toString()) : null;

        this.acquiredExist1 = XmlLoader.isNewFormatGutdf() ? new StringElement("") : null;

        this.inhabitedExist0 = XmlLoader.isNewFormatGutdf() && (!isHomePledge) ? new StringElement("") : null;

        this.inhabitedExist1 = XmlLoader.isNewFormatGutdf() && (isHomePledge) ? new StringElement("") : null;
    }

    public StringElement getCode() {
        return code;
    }

    public StringElement getDate() {
        return date == null ? startDate : date;
    }

    public List<SumGroupUl23_26Group> getSumGroupUl23_26Group() {
        return sumGroupUl23_26Group;
    }

    public StringElement getCollateralEndDate() {
        return collateralEndDate;
    }

    public StringElement getCollateralFactEndDate() {
        return collateralFactEndDate;
    }

    public StringElement getEndReason() {
        return endReason;
    }

    public StringElement getContractTotalSum() {
        return contractTotalSum;
    }

    public StringElement getContractCount() {
        return contractCount;
    }

    public StringElement getOkato() {
        return okato;
    }

    public StringElement getActualCost() {
        return actualCost;
    }

    public StringElement getCalcDate() {
        return calcDate;
    }
}
