package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.sql.Date;
import java.sql.Timestamp;

@XStreamAlias("Закрыт")
public final class AccountClose {

    @XStreamAlias("КодСостСч")
    private final Integer codeState = 0;

    @XStreamAlias("ДатаЗакрСч")
    private String dateCloseAccount;

    @XStreamAlias("КодСостДог")
    private Integer codeStatePact = 1;

    @XStreamAlias("ДатаРастДог")
    private String dateClosePact;

    public AccountClose(Date dateClose) {

        dateCloseAccount = MainDocument.dateFormat(dateClose);

        dateClosePact = dateCloseAccount;
    }
}
