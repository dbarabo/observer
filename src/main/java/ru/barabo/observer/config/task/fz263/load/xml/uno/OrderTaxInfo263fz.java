package ru.barabo.observer.config.task.fz263.load.xml.uno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.Fns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderTaxInfo263fz implements FromFnsInfo {

    @XStreamAlias("НомПоруч")
    private String mainNumber;

    @XStreamAlias("ДатаПоруч")
    private String mainDate;

    @XStreamAlias("СумПлат")
    private String mainSumKopeika;

    @XStreamAlias("Статус")
    private String mainStatus;

    @XStreamAlias("ИНННП")
    private String payerInn;

    @XStreamAlias("КППНП")
    private String payerKpp; // always is empty

    @XStreamAlias("Плательщ")
    private String payerName;

    @XStreamAlias("НомСчПл")
    private String account; // always single

    @XStreamAlias("БанкПл")
    private String bankName;

    @XStreamAlias("БИКБПл")
    private String bankBik;

    @XStreamAlias("БанкПол")
    private String benBankName;

    @XStreamAlias("БИКБПол")
    private String benBankBik;

    @XStreamAlias("ИННПол")
    private String benInn;

    @XStreamAlias("КПППол")
    private String benKpp;

    @XStreamAlias("Получ")
    private String benName;

    @XStreamAlias("НомСчПол")
    private String benAccount;

    @XStreamAlias("ВидОп")
    private String mainType;

    @XStreamAlias("ОчерПл")
    private String queuePay;

    @XStreamAlias("КодПл")
    private String codePay; //UID from fns_from

    @XStreamAlias("РезПоле")
    private String reservFieldVersion; //version if UNO as number integer

    @XStreamAlias("НазнПл")
    private String mainDescription;

    @XStreamAlias("ВидПор")
    private String vidPorPay;

    @XStreamAlias("УНКГН")
    private String unkgn;

    @XStreamAlias("КБК")
    private String kbkPay;

    @XStreamAlias("ОКТМО")
    private String oktmoPay; // always is equals 0

    @XStreamAlias("КодОсн")
    private String codeOsnPay; // May be is equals 0

    @XStreamAlias("СрокУплТр")
    private String srokTrebPay; // May be is equals 0

    @XStreamAlias("НомТреб")
    private String addNumber; // May be is equals 0

    @XStreamAlias("ДатаТреб")
    private String addDate; // May be is equals 0

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

    @Override
    public Fns getFns() {
        return new Fns() {

            @Override
            public String getNameFns() {
                return null;
            }

            @Override
            public String getFnsCode() {
                return null;
            }
        };
    }

    @Override
    public ParamsQuery getPayer() {
        return new PayerJur(payerInn, payerKpp, payerName);
    }

    @Override
    public String getAccounts() {
        return account;
    }

    @Override
    public java.sql.Date getMainDate() {

        return XmlLoader.parseDate(mainDate);
    }

    @Override
    public String getMainNumber() {
        return mainNumber;
    }

    @Override
    public Number getMainSum() {

        return Long.parseLong(mainSumKopeika);
    }

    @Override
    public java.sql.Date getAddDate() {
        return null;
    }

    @Override
    public String getAddNumber() {
        return addNumber;
    }

    @Override
    public String getCards() {
        return null;
    }

    @Override
    public String getCardsCurrency() {
        return null;
    }

    @Override
    public String getMainStatus() {
        return mainStatus;
    }

    @Override
    public String getMainType() {
        return mainType;
    }

    @Override

    public String getMainDescription() {
        return mainDescription;
    }

    @Override
    public String getSubNumber() {
        return null;
    }

    @Override
    public Date getSubDate() {
        return null;
    }

    @Override
    public String getMainCode() {
        return null;
    }

    @Override
    public String getAccountsStartDates() {
        return "";
    }

    @Override
    public String getAccountsEndDates() {
        return "";
    }

    @Override
    public String getUID() {
        return codePay;
    }

    public ParamsQuery getUnoData() {
        return new ParamsQuery() {

            private static final String COLUMNS = "BEN_BANK_NAME, BEN_BANK_BIK, BEN_INN, BEN_KPP, BEN_NAME, BEN_ACCOUNT, "
                    + "QUEUE_PAY, CODE_PAY, VIDPOR_PAY, UNKGN_PAY, KBK_PAY, OKTMO_PAY, CODEOSN_PAY, "
                    + "SROK_TREB_PAY, DATE_TREB_PAY, NUMBER_TREB_PAY, "
                    + "FNS_FROM, ID";
            @Override
            public List<Object> getParams() {
                return new ArrayList<> (Arrays.asList(
                        toStr(benBankName),
                        toStr(benBankBik),
                        toStr(benInn),
                        toStr(benKpp),
                        toStr(benName),
                        toStr(benAccount),
                        toStr(queuePay),
                        toStr(reservFieldVersion), // reserv in CODE_PAY!!!
                        toStr(vidPorPay),
                        toStr(unkgn),
                        toStr(kbkPay),
                        toStr(oktmoPay),
                        toStr(codeOsnPay),
                        toStr(srokTrebPay),
                        toStr(addDate),
                        toStr(addNumber) ) );
            }

            @Override
            public String getListColumns() {
                return COLUMNS;
            }

            private String toStr(String value) {
                return value == null ? "" : value;
            }
        };
    }
}
