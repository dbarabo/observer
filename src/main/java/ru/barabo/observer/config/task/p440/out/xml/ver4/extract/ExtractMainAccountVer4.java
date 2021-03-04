package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.out.xml.ver4.CurrencyAccount;
import ru.barabo.observer.config.task.p440.out.xml.ver4.TypeAccountVer4;

import java.util.Date;
import java.util.List;

@XStreamAlias("СведСЧОпер")
public class ExtractMainAccountVer4 {

    @XStreamAlias("НомСч")
    private String code;

    @XStreamAlias("ДатаОткр")
    private String opened;

    @XStreamAlias("ДатаОкон")
    private String depositEnd;

    @XStreamAlias("ДатаЗакр")
    private String closed;

    @XStreamAlias("ПорНом")
    private String orderFile;

    @XStreamAlias("ДатаНачПериод")
    private String startExtract;

    @XStreamAlias("ДатаОкПериод")
    private String endExtract;

    @XStreamAlias("ВалютаСч")
    private CurrencyAccount currency;

    @XStreamAlias("ВидСч")
    private TypeAccountVer4 typeAccount;

    @XStreamAlias("СвОстОборотСЧ")
    private GroupTurnExtractAccount turnExtractAccount;

    @XStreamAlias("СведДопФайл")
    private GroupInfoAddFile groupInfoAddFile;

    public ExtractMainAccountVer4(String code, Date opened, Date depositEnd, Date closed, Integer orderFile,
                                  Date startPeriod, Date endPeriod, String currency, String typeAccountCode,
                                  String typeAccountName, Number startRest, Number endRest, Number debet, Number credit,
                                  Boolean isAbsentTurn) {

        this.code = code;

        this.opened = XmlLoader.formatDate(opened);

        this.depositEnd = XmlLoader.formatDate(depositEnd);

        this.closed = XmlLoader.formatDate(closed);

        this.orderFile = String.format("%06d", orderFile);

        this.startExtract = XmlLoader.formatDate(startPeriod);

        this.endExtract = XmlLoader.formatDate(endPeriod);

        this.currency = new CurrencyAccount(currency);

        this.typeAccount = new TypeAccountVer4(typeAccountCode, typeAccountName);

        this.turnExtractAccount = new GroupTurnExtractAccount(startRest, endRest, debet, credit);

        if(isAbsentTurn) {
            groupInfoAddFile = GroupInfoAddFile.absentAddFile();
        }
    }

    public GroupInfoAddFile getGroupInfoAddFile() {
        return groupInfoAddFile;
    }

    public void addGroupInfoAddFile(List<String> filenames) {
        groupInfoAddFile = new GroupInfoAddFile(filenames);
    }

    public String getCode() {

        return code;
    }

    public java.sql.Date getStartExtract() {

        return XmlLoader.parseDate(startExtract);
    }

    public java.sql.Date getEndExtract() {

        return XmlLoader.parseDate(endExtract);
    }

    public String getCodeTypeAccount() {
        return typeAccount.getCode();
    }

    public String getOrderFile() {
        return orderFile;
    }
}
