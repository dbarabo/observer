package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AccountDatePeriod;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoBankPBRVer4;

import java.util.List;

@XStreamAlias("СведСчет")
public class ExistsInfoDetailVer4 {

    @XStreamAlias("СчДатаПериод")
    private AccountDatePeriod restAccountDatePeriod;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = new InfoBankPBRVer4(BankXml.ourBankToo());

    @XStreamImplicit(itemFieldName = "Счет")
    private List<ExistsAccountVer4> existsAccountsVer4;


    public ExistsInfoDetailVer4(ExistsResponseDataVer4 existsResponseData) {

        if(existsResponseData.getOnStateDateRequest() != null ||
                existsResponseData.getStartPeriodRequest() != null) {
            restAccountDatePeriod = new AccountDatePeriod(existsResponseData);
        }

        this.existsAccountsVer4 = existsResponseData.getExistsAccountsVer4();
    }
}
