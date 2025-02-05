package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData;
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseData;
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountAbsent;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountDatePeriod;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoBankPBRVer4;

import java.util.List;

@XStreamAlias("СведенияВып")
public class InfoExtractDetail {
    @XStreamAlias("СчДатаПериод")
    private AccountDatePeriod accountDatePeriod;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = new InfoBankPBRVer4(BankXml.ourBankToo());// InfoBankPBRVer4.ourBank();

    @XStreamImplicit(itemFieldName = "СведСЧОпер")
    private List<ExtractMainAccountVer4> mainAccounts;

    @XStreamImplicit(itemFieldName = "СчетОтсут")
    private List<AccountAbsent> absentAccounts;

    public InfoExtractDetail(ExtractResponseData extractMainResponseData) {

        if(extractMainResponseData.getOnStateDateRequest() != null ||
                extractMainResponseData.getStartPeriodRequest() != null) {
            accountDatePeriod = new AccountDatePeriod(extractMainResponseData);
        }

        this.mainAccounts = extractMainResponseData.getMainAccountList();

        this.absentAccounts = extractMainResponseData.getAccountAbsentList();
    }
}
