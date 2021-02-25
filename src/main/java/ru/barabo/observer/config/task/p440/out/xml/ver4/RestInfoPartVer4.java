package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart;
import ru.barabo.observer.config.task.p440.out.xml.rest.RestAccount;

import java.util.Date;
import java.util.List;

@XStreamAlias("СПРОБОСТАТ")
public class RestInfoPartVer4 extends AbstractInfoPart {

    @XStreamAlias("НомСправ")
    private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

    @XStreamAlias("ТипСправ")
    private String typeHelp = "1";

    @XStreamAlias("НомЗР")
    private String requestNumber;

    @XStreamAlias("ДатаЗР")
    private String requestDate;

    @XStreamAlias("ВидСпр")
    private String viewHelp; // берем из базы

    @XStreamAlias("ДейстПоСост")
    private String actualDate = XmlLoader.formatDate(new Date());

    @XStreamImplicit(itemFieldName = "Остатки")
    private List<RestAccount> restList;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public RestInfoPartVer4(RestResponseData restResponseData) {
        super(restResponseData);

        this.viewHelp = restResponseData.getViewHelp();

        this.restList = restResponseData.getRestAccountList();
    }

    @Override
    protected void setDateRequest(String dateRequest) {
        this.requestDate = dateRequest;
    }

    @Override
    protected void setNumberRequest(String numberRequest) {
        this.requestNumber = numberRequest;
    }
}
