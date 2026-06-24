package ru.barabo.observer.config.task.fz263.load.xml.upv;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.*;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

import java.sql.Date;
import java.util.List;

@XStreamAlias("ПОРПРОДВАЛ")
public class SaleCurrency extends AbstractFromFnsInfo {

    @XStreamAlias("БИК")
    private String bankBik;

    @XStreamAlias("НаимБанк")
    private String bankName;

    @XStreamAlias("НомПоруч")
    private String numberOrder;

    @XStreamAlias("ДатаПоруч")
    private String dateOrder;

    @XStreamAlias("УИДПоруч")
    private String uuidOrder;

    @XStreamAlias("КодНО")
    private String codeFns;

    @XStreamAlias("НомСч")
    private String account;

    @XStreamAlias("ВидСч")
    private String accountType;

    @XStreamAlias("НомСчЗач")
    private String accountTo;

    @XStreamAlias("ВидСчЗач")
    private String accountTypeTo;

    @XStreamImplicit(itemFieldName = "Поручения")
    private List<OrderUno> orderList;

    @Override
    public String getUID() {
        return uuidOrder;
    }

    @Override
    public String getMainNumber() {
        return numberOrder;
    }

    @Override
    public Date getMainDate() {
        return XmlLoader.parseDate(dateOrder);
    }

    @Override
    public String getMainCode() {
        return null;
    }

    @Override
    public String getMainDescription() {
        return "";
    }

    @Override
    public Number getMainSum() {
        return null;
    }

    @Override
    public String getMainType() {
        return "";
    }

    @Override
    public String getMainStatus() {
        return "";
    }

    @Override
    public String getAccounts() {
        return account;
    }

    @Override
    public String getCards() {
        return "";
    }

    @Override
    public String getCardsCurrency() {
        return "";
    }

    @Override
    public String getAddNumber() {
        return accountTo;
    }

    @Override
    public Date getAddDate() {
        return null;
    }

    @Override
    public String getSubNumber() {

        return orderList.get(0).getNumberUno();
    }

    @Override
    public Date getSubDate() {

        return orderList.get(0).getDateUno();
    }

    @Override
    public FnsXml getFns() {
        return new FnsXml() {

            @Override
            public String getNameFns() {
                return null;
            }

            @Override
            public String getFnsCode() {
                return codeFns;
            }
        };
    }

    @Override
    public Bank getBank() {
        return new Bank() {

            @Override
            public String getBik() {
                return bankBik;
            }

            @Override
            public String getName() {
                return bankName;
            }
        };
    }
}
