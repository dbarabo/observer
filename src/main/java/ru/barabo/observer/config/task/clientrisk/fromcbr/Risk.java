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

    @XStreamOmitField
    private Object MainRisk; // MainRisk

    @XStreamOmitField
    private Object AddRisk1; // AddRisk1

    @XStreamOmitField
    private Object AddRisk2; // AddRisk2

    @XStreamOmitField
    private Object AddRisk3; // AddRisk3

    public String getInn() {
        return inn;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public String getRiskDate() {
        return riskDate;
    }
}
