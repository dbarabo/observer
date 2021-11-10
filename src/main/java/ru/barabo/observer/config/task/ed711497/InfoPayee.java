package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

import java.util.Date;

@XStreamAlias("tns:СведФЛ")
public class InfoPayee {

    @XStreamAlias("Гражд")
    final private String citizen;

    @XStreamAlias("ДатаРожд")
    final private String birthday;

    @XStreamAlias("ИННФЛ")
    final private String inn;

    @XStreamAlias("СумПроц")
    final private String amountPercent;

    @XStreamAlias("СумПроц")
    final private String amountPercentIP;

    @XStreamAlias("ФИО")
    final private Fio fio;

    @XStreamAlias("УдЛичнФЛ")
    final private InfoDocument document;

    public InfoPayee(Integer citizen, Date birthday, String inn, Number amountPercent, Number amountPercentIP,
                     String firstName, String lastName, String secondName,
                     String code, String lineNumber, Date dateOut) {

        this.citizen = citizen.toString();

        this.birthday = XmlLoader.formatDate(birthday);

        this.inn = inn;

        this.amountPercent = XmlLoader.formatSum(amountPercent);

        this.amountPercentIP = XmlLoader.formatSum(amountPercentIP);

        this.fio = new Fio(firstName, lastName, secondName);

        this.document = new InfoDocument(code, lineNumber, dateOut);
    }

}
