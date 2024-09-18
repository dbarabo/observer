package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("tns:Подписант")
public class Signer {

    @XStreamAlias("Должн")
    private final String positionMainBank = "Начальник ООК";

    @XStreamAlias("Тлф")
    private final String phoneMainBank = "(423)220-96-93";

    @XStreamAlias("ДатаПодп")
    private final String dateMessage = XmlLoader.formatDate(new Date());


    @XStreamAlias("tns:ФИО")
    private final Fio fio = new Fio("Анастасия", "Шабот",  "Дмитриевна");

}
