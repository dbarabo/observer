package ru.barabo.observer.config.task.nbki.gutdf.legal.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

/**
 * Блок 30. Сведения о судебном споре или требовании по обязательству
 */
public class Ul30Court {

    @XStreamAlias("exist_0")
    private final StringElement exist0; // 39.1. Признак судебного спора или требования = 0

    @XStreamAlias("exist_1")
    private final StringElement exist1; // 39.1. Признак судебного спора или требования = 1

    @XStreamAlias("actExist_0")
    private final StringElement actExist0; // 39.2. Признак наличия судебного акта = 0

    @XStreamAlias("actExist_1")
    private final StringElement actExist1; // 39.2. Признак наличия судебного акта = 1

    @XStreamAlias("date")
    private final StringElement date; // 39.3. Дата принятия судебного акта

    @XStreamAlias("num")
    private final StringElement num; //39.4. Номер судебного акта

    @XStreamAlias("actResolution")
    private final StringElement actResolutionCode; //39.5. Резолютивная часть судебного акта

    @XStreamAlias("actStartExist_0")
    private final StringElement actStartExist0; // 39.6. Признак вступления акта в законную силу = 0

    @XStreamAlias("actStartExist_1")
    private final StringElement actStartExist1; // 39.6. Признак вступления акта в законную силу = 1

    @XStreamAlias("lawsuitCode")
    private final StringElement lawsuitCode; // 39.7. Код иска или требования

    @XStreamAlias("sumTotal")
    private final StringElement sumTotal; // emptyValueType dec15p2Type 39.8. Сумма требований, подлежащих удовлетворению

    @XStreamAlias("info")
    private final StringElement info; // minOccurs="0" 39.9. Дополнительные сведения по судебному акту

    /**
     * @param isExistsDispute - только спор суд. акта в любом случае нет
     */
    public Ul30Court(Boolean isExistsDispute) {

        if(isExistsDispute) {
            this.exist0 = null;
            this.exist1 = new StringElement("");

            this.actExist0 = new StringElement("");

        } else {
            this.exist0 = new StringElement("");
            this.exist1 = null;

            this.actExist0 = null;
        }

        this.actExist1 = null;
        this.date = null;
        this.num = null;
        this.actResolutionCode = null;
        this.actStartExist0 = null;
        this.actStartExist1 = null;
        this.lawsuitCode = null;
        this.sumTotal = null;
        this.info = null;
    }

    public Ul30Court(Date date, String num, Integer actResolutionCode, Boolean isActStarted,
                     Integer lawsuitCode, Number sumTotal, String info) {

        this.exist0 = null;
        this.exist1 = new StringElement("");

        this.actExist0 = null;
        this.actExist1 = new StringElement("");

        this.date = new StringElement(XmlLoader.formatDate(date));

        this.num = new StringElement(num);

        this.actResolutionCode = new StringElement(actResolutionCode.toString());

        if(isActStarted) {
            this.actStartExist0 = null;
            this.actStartExist1 = new StringElement("");
        } else {
            this.actStartExist0 = new StringElement("");
            this.actStartExist1 = null;
        }

        this.lawsuitCode = new StringElement(lawsuitCode.toString());

        this.sumTotal = new StringElement(sumTotal == null ? "-" : XmlLoader.formatSum(sumTotal));

        this.info = info == null ? null : new StringElement(info);
    }

    public String getActExist() { // 0 1 2

        if(exist0 == null && exist1 == null) return "2";

        return exist0 == null ? "1" : "0";
    }

    public StringElement getDate() {
        return date;
    }

    public StringElement getNum() {
        return num;
    }

    public StringElement getActResolutionCode() {
        return actResolutionCode;
    }

    public Boolean isActStarted() {
        return actStartExist1 != null;
    }

    public StringElement getLawsuitCode() {
        return lawsuitCode;
    }

    public StringElement getSumTotal() {
        return sumTotal;
    }

    public StringElement getInfo() {
        return info;
    }
}
