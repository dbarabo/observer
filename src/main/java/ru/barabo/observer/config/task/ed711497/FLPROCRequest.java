package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.skad.forms.ed711497.PercentOutData;

import java.util.List;
import java.util.UUID;

@XStreamAlias("tns:FLPROCRequest")
public class FLPROCRequest {
    @XStreamAsAttribute
    @XStreamAlias("xmlns:tns")
    final String xmlns = "urn://x-artefacts-fns-flproc/root/310-68/4.0.2";

    @XStreamAlias("ИдЗапрос")
    final private String idRequest = UUID.randomUUID().toString().toUpperCase();

    @XStreamAlias("Период")
    final private String yearPeriod;

    @XStreamAlias("КодНО")
    final private String codeFns;

    @XStreamAlias("tns:СведБанк")
    final private BankInfo bankInfo = new BankInfo();

    @XStreamImplicit(itemFieldName = "tns:СведПроц")
    final private List<InfoPercent> infoPercents;

    @XStreamAlias("tns:Подписант")
    final private Signer signer = new Signer();

    public FLPROCRequest(PercentOutData percentOutData) {

        yearPeriod = percentOutData.getYearReport();

        codeFns = percentOutData.getCodeFns();

        infoPercents = percentOutData.getInfoPercents();
    }
}
