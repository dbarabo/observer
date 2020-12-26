package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@XStreamAlias("СвСчет")
public final class SvAccount {

    @XStreamAlias("НомСч")
    private String code;

    @XStreamAlias("ДатаОткрСч")
    private String dateOpen;

    @XStreamAlias("КодСч")
    private String typeAccount;

    @XStreamAlias("ВалСч")
    private Integer currency;

    @XStreamAlias("НазнСч")
    private Integer isPhysic; // only physic=1 - juric must be null

    @XStreamAlias("Открыт")
    private AccountOpen accountOpen;

    @XStreamAlias("Закрыт")
    private AccountClose accountClose;

    public SvAccount(String code, Date dateOpen, String typeAccount, Integer currency,
                     Integer isPhysic, Boolean isOpened, String numberPact, Date dateOpenClose) {

        this.code = code;
        this.dateOpen = MainDocument.dateFormat(dateOpen);
        this.typeAccount = typeAccount;
        this.currency = currency;

        this.isPhysic = isPhysic;

        if(isOpened) {
            accountOpen = new AccountOpen(dateOpenClose, numberPact);
        } else {
            accountClose = new AccountClose(dateOpenClose);
        }
    }
}
