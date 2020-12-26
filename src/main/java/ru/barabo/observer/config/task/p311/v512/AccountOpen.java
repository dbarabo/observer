package ru.barabo.observer.config.task.p311.v512;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.sql.Date;
import java.sql.Timestamp;

@XStreamAlias("Открыт")
public final class AccountOpen {

    @XStreamAlias("КодСостСч")
    private final Integer codeState = 1;

    @XStreamAlias("НомерДог")
    private String numberPact;

    @XStreamAlias("КодСостДог")
    private final Integer codeStatePact = 0;

    @XStreamAlias("ДатаЗаклДог")
    private String dateOpenPact;

    public AccountOpen(Date dateOpen, String numberPact) {

        this.numberPact = numberPact;

        dateOpenPact = MainDocument.dateFormat(dateOpen);
    }
}
