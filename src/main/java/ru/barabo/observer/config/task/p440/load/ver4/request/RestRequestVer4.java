package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.request.AbstractRequest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@XStreamAlias("ЗАПНООСТАТ")
public class RestRequestVer4 extends AbstractRequest {

    @XStreamAlias("ПоВсем")
    private RestTypeAllAccounts typeAllAccounts;

    @XStreamImplicit(itemFieldName = "ПоУказанным")
    private List<RestTypeDetailAccounts> typeDetailAccounts;

    @Override
    public java.sql.Date getAddDate() {

        if(typeAllAccounts != null && typeAllAccounts.getOnStateDate() != null) {
            return typeAllAccounts.getOnStateDate();
        }

        Optional<RestTypeDetailAccounts> account = typeDetailAccounts.stream().filter(acc -> acc.getOnDate() != null).findFirst();

        return account.get().getOnDate();
    }

    @Override
    public String getAccounts() {
        if ((typeAllAccounts != null && typeAllAccounts.getOnStateDate() != null) ||
                typeDetailAccounts == null ||
                typeDetailAccounts.size() == 0) {
            return null;
        }

        return typeDetailAccounts.stream().map(RestTypeDetailAccounts::getCode).collect(Collectors.joining(";"));
    }

    @Override
    public Date getSubDate() {
        if (typeAllAccounts != null && typeAllAccounts.getOnStateDate() != null) {
            return typeAllAccounts.getOpenAccountDate();
        }

        return null;
    }
}
