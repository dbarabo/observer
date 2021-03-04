package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.out.xml.extract.DocumentInfo;

import java.sql.Timestamp;
import java.util.Date;

@XStreamAlias("ОперацииСЧ")
public class OperationAccountVer4 {

    @XStreamAlias("ПорНомБлок")
    private String orderOper;

    @XStreamAlias("ДатаОпер")
    private String operDate;

    @XStreamAlias("ВремяОпер")
    private String operTime;

    @XStreamAlias("ПопМСКВремя")
    final private String mskShiftTime = "+07";

    @XStreamAlias("СумДебета")
    private String debet;

    @XStreamAlias("СумКредита")
    private String credit;

    @XStreamAlias("НазПлат")
    private String description;

    @XStreamAlias("РеквДок")
    private DocumentInfo documentInfo;

    @XStreamAlias("РеквБанк")
    private BankInfoVer4 bankInfo;

    @XStreamAlias("РеквПлат")
    private PayerInfoVer4 payerInfo;

    public OperationAccountVer4(Integer orderOper, Timestamp operDate, Number debet, Number credit, String description,
                                String vid, String number, Date date,
                                String corrAccount, String bankName, String bik,
                                String payerName, String payerInn, String payerKpp, String payerAccount) {

        this.orderOper = String.format("%06d", orderOper);

        this.operDate = XmlLoader.formatDate(operDate);

        this.operTime = XmlLoader.formatTime(operDate);

        this.debet = XmlLoader.formatSum(debet);

        this.credit = XmlLoader.formatSum(credit);

        this.description = description;

        this.documentInfo = new DocumentInfo(vid, number, date);

        this.bankInfo = new BankInfoVer4(corrAccount, bankName, bik);

        this.payerInfo = new PayerInfoVer4(payerName, payerInn, payerKpp, payerAccount);
    }
}
