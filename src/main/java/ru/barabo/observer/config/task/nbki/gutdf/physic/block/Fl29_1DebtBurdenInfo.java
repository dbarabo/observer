package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("FL_29_1_DebtBurdenInfo")
public class Fl29_1DebtBurdenInfo {

    @XStreamAlias("loadRange")
    private final StringElement loadRange; // 29(1).1. Диапазон показателя долговой нагрузки заемщика 1-7

    @XStreamAlias("loadCalcDate")
    private final StringElement loadCalcDate; // 29(1).1. Диапазон показателя долговой нагрузки заемщика 1-7

    @XStreamAlias("incomeInfo")
    private final StringElement incomeInfo; // 29(1).3. Сведения о способе определения величины среднемесячного дохода заемщика

    @XStreamImplicit(itemFieldName = "incomeInfoSource")
    private final List<StringElement> incomeInfoSourceList; // 29(1).4. Источник сведений о величине среднемесячного дохода заемщика

    @XStreamAlias("loadFact_0")
    private final StringElement loadFact0; // 29(1).5. Признак учета долговой нагрузки созаемщиков = 0

    @XStreamAlias("loadFact_1")
    private final StringElement loadFact1; // 29(1).5. Признак учета долговой нагрузки созаемщиков = 1

    @XStreamAlias("loadCalculationFact_0")
    private final StringElement loadCalculationFact0; // 29(1).6. Признак расчета показателя долговой нагрузки заемщика по заключенному потребительскому кредиту (займу)  = 0

    @XStreamAlias("loadCalculationFact_1")
    private final StringElement loadCalculationFact1; // 29(1).6. Признак расчета показателя долговой нагрузки заемщика по заключенному потребительскому кредиту (займу)  = 1

    @XStreamAlias("dealUid")
    private final StringElement dealUID; //29(1).7. УИд сделки (УИд обращения)

    public Fl29_1DebtBurdenInfo(String loadRange, String loadCalcDate, String incomeInfo, String incomeInfoSource,
                                Boolean isLoadFact, Boolean isLoadCalculationFact, String dealUID) {

        this.loadRange = new StringElement(loadRange);

        this.loadCalcDate = new StringElement(loadCalcDate);

        this.incomeInfo = new StringElement(incomeInfo);

        this.incomeInfoSourceList = new ArrayList<>();
        this.incomeInfoSourceList.add(new StringElement(incomeInfoSource));

        if(isLoadFact) {
            this.loadFact0 = null;
            this.loadFact1 = new StringElement("");
        } else {
            this.loadFact0 = new StringElement("");
            this.loadFact1 = null;
        }

        if(isLoadCalculationFact) {
            this.loadCalculationFact0 = null;
            this.loadCalculationFact1 = new StringElement("");
        } else {
            this.loadCalculationFact0 = new StringElement("");
            this.loadCalculationFact1 = null;
        }

        this.dealUID = new StringElement(dealUID);
    }

    public StringElement getLoadRange() {
        return loadRange;
    }

    public StringElement getLoadCalcDate() {
        return loadCalcDate;
    }

    public StringElement getIncomeInfo() {
        return incomeInfo;
    }

    public List<StringElement> getIncomeInfoSourceList() {
        return incomeInfoSourceList;
    }

    public Boolean isLoadFact() {
        if(loadFact0 == null && loadFact1 == null) return null;

        return loadFact1 != null;
    }

    public Boolean isLoadCalculationFact() {
        if(loadCalculationFact0 == null && loadCalculationFact1 == null) return null;

        return loadCalculationFact1 != null;
    }

    public StringElement getDealUID() {
        return dealUID;
    }
}
