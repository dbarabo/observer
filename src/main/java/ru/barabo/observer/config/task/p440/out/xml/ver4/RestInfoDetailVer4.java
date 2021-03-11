package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

import java.util.List;

@XStreamAlias("СведенияОст")
public class RestInfoDetailVer4 {

    @XStreamAlias("СчДатаПериод")
    private AccountDatePeriod restAccountDatePeriod;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = new InfoBankPBRVer4(BankXml.ourBankToo());

    @XStreamImplicit(itemFieldName = "Счет")
    private List<RestAccountVer4> restAccountsVer4;

    @XStreamImplicit(itemFieldName = "СчетОтсут")
    private List<AccountAbsent> accountAbsents;

    public RestInfoDetailVer4(RestResponseDataVer4 restResponseData) {

        if(restResponseData.getOnStateDateRequest() != null ||
                restResponseData.getStartPeriodRequest() != null) {
            restAccountDatePeriod = new AccountDatePeriod(restResponseData);
        }

        this.restAccountsVer4 = restResponseData.getRestAccountsVer4();

        this.accountAbsents = restResponseData.getAccountAbsents();
    }
}
