package ru.barabo.observer.config.task.p440.load.ver4.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.request.AbstractRequest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@XStreamAlias("ЗАПНОВЫПИС")
public class ExtractRequestVer4 extends AbstractRequest {

    @XStreamAlias("ПоВсем")
    private TypeAllAccounts typeAllAccounts;

    @XStreamImplicit(itemFieldName = "ПоУказанным")
    private List<TypeDetailAccounts> typeDetailAccounts;

    @Override
    public java.sql.Date getAddDate() {

        if(typeAllAccounts != null && typeAllAccounts.getStartDate() != null) {
            return typeAllAccounts.getStartDate();
        }

        Optional<TypeDetailAccounts> account = typeDetailAccounts.stream().filter(acc -> acc.getStartDate() != null).findFirst();

        return account.map(TypeDetailAccounts::getStartDate).orElse(null);
    }

    @Override
    public Date getSubDate() {
        if (typeAllAccounts != null && typeAllAccounts.getEndDate() != null) {
            return typeAllAccounts.getEndDate();
        }

        Optional<TypeDetailAccounts> account = typeDetailAccounts.stream().filter(acc -> acc.getEndDate() != null).findFirst();

        return account.map(TypeDetailAccounts::getEndDate).orElse(null);
    }

    @Override
    public String getAccounts() {
        if (isAllAccountsPeriod()) {
            return null;
        }

        return typeDetailAccounts.stream().map(TypeDetailAccounts::getCode).collect(Collectors.joining(";"));
    }

    @Override
    public String getAccountsStartDates() {
        if (isAllAccountsPeriod()) {
            return "";
        }

        return typeDetailAccounts.stream().map(TypeDetailAccounts::getShorStartDate).collect(Collectors.joining(";"));
    }

    @Override
    public String getAccountsEndDates() {
        if (isAllAccountsPeriod()) {
            return "";
        }

        return typeDetailAccounts.stream().map(TypeDetailAccounts::getShorEndDate).collect(Collectors.joining(";"));
    }

    private boolean isAllAccountsPeriod() {
        return ((typeAllAccounts != null && typeAllAccounts.getStartDate() != null) ||
                typeDetailAccounts == null ||
                typeDetailAccounts.size() == 0);
    }
}
