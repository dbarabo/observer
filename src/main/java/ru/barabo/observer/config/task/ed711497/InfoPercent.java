package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Date;

@XStreamAlias("tns:СведПроц")
public class InfoPercent {

    @XStreamAlias("ПорядНом")
    final private String idOrderNumber;

    @XStreamAlias("НомКорр")
    final private String correctionNumber;

    @XStreamAlias("ПрКорр")
    final private String propertyCorrection;

    @XStreamAlias("tns:СведФЛ")
    final private InfoPayee infoPayee;

    public InfoPercent(Integer idOrderNumber, String correctionNumber, String propertyCorrection,
            Integer citizen, Date birthday, String inn, Number amountPercent, Number amountPercentIP,
                       String firstName, String lastName, String secondName,
                       String code, String lineNumber, Date dateOut) {

        this.idOrderNumber = idOrderNumber.toString();

        this.correctionNumber = correctionNumber;

        this.propertyCorrection = propertyCorrection;

        this.infoPayee = new InfoPayee(citizen, birthday, inn, amountPercent, amountPercentIP,
                firstName, lastName, secondName,
                code, lineNumber, dateOut);
    }
}
