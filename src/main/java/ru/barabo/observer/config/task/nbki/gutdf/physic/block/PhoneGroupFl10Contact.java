package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class PhoneGroupFl10Contact {

    @XStreamAlias("phone")
    private final StringElement phone; //10.1. Номер телефона "((\+\d{1,3}\(\d{1,5}\)\d{3,9})|\d{5,17})"

    @XStreamAlias("phoneComment")
    private final StringElement phoneComment; //10.2. Комментарий к номеру телефона minOccurs="0"

    public PhoneGroupFl10Contact(String phone) {

        this.phone = new StringElement(phone);

        phoneComment = null;
    }
}
