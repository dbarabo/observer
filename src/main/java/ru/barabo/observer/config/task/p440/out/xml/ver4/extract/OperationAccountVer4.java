package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.out.xml.extract.AmountInfo;
import ru.barabo.observer.config.task.p440.out.xml.extract.BankInfo;
import ru.barabo.observer.config.task.p440.out.xml.extract.DocumentInfo;
import ru.barabo.observer.config.task.p440.out.xml.extract.PayerInfo;

@XStreamAlias("ОперацииСЧ")
public class OperationAccountVer4 {

    @XStreamAlias("ПорНомБлок")
    private Integer orderFile;

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

    @XStreamAlias("НазнПл")
    private String description;

    @XStreamAlias("РеквДок")
    private DocumentInfo documentInfo;

    @XStreamAlias("РеквБанка")
    private BankInfo bankInfo;

    @XStreamAlias("РеквПлат")
    private PayerInfo payerInfo;

    @XStreamAlias("СуммаОпер")
    private AmountInfo amountInfo;
}
