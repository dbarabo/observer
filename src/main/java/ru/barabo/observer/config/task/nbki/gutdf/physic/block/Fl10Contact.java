package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.ArrayList;
import java.util.List;

public class Fl10Contact {

    @XStreamAlias("phone")
    private final StringElement phone; //10.1. Номер телефона "((\+\d{1,3}\(\d{1,5}\)\d{3,9})|\d{5,17})"

    @XStreamAlias("phoneComment")
    private final StringElement phoneComment; //10.2. Комментарий к номеру телефона minOccurs="0"

    @XStreamAlias("email")
    final StringElement email; //  10.3. Адрес электронной почты

    public Fl10Contact(String element) {

        if( element.contains("@") ){
            email = new StringElement(element);
            phone = null;
            phoneComment = null;
        } else {
            phone = new StringElement(element);
            phoneComment = null;

            email = null;
        }
    }

    public StringElement getPhone() {
        return phone;
    }

    public StringElement getEmail() {
        return email;
    }

}
