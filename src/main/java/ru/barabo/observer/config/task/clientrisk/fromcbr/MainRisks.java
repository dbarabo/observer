package ru.barabo.observer.config.task.clientrisk.fromcbr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("RISKS")
public class MainRisks {

    @XStreamAlias("UniqueIdentifier")
    private String uniqueIdentifier;

    @XStreamAlias("Risks_Report_Date")
    private String risksReportDate; // 2022-04-11

    @XStreamImplicit(itemFieldName = "RISK")
    private List<Risk> risks;

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public String getRisksReportDate() {
        return risksReportDate;
    }

    public List<Risk> getRisks() {
        return risks;
    }
}
