package ru.barabo.observer.config.task.clientrisk.fromcbr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("RISK")
public class Risk {

    @XStreamAlias("inn")
    private String inn;

    @XStreamAlias("client_type")
    private String clientType;

    @XStreamAlias("risk_level")
    private Integer riskLevel;

    @XStreamAlias("risk_date")
    private String riskDate; // 2022-04-06

    @XStreamAlias("MainRisk")
    private String mainRisk;

    //@XStreamOmitField
    @XStreamAlias("AddRisk1")
    private String addRisk1;

    @XStreamAlias("AddRisk2")
    private String addRisk2;

    @XStreamAlias("AddRisk3")
    private String addRisk3;

    public String getInn() {
        return inn;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public String getRiskDate() {
        return riskDate;
    }

    public String getMainRisk() {
        return mainRisk;
    }

    public String getAddRisk1() {
        return addRisk1;
    }

    public String getAddRisk2() {
        return addRisk2;
    }

    public String getAddRisk3() {
        return addRisk3;
    }

    @Override public String toString() {
        return "inn="+ inn + " clientType=" + clientType + " riskLevel=" + riskLevel + " risk_date=" + riskDate +
                " mainRisk=" + mainRisk + " addRisk1=" + addRisk1 + " addRisk2=" + addRisk2 + " addRisk2=" + addRisk2;
    }
}
