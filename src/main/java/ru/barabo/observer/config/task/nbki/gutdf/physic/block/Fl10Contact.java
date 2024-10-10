package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.ArrayList;
import java.util.List;

public class Fl10Contact {

    @XStreamImplicit(itemFieldName = "Phone_group_FL_10_Contact")
    final List<PhoneGroupFl10Contact> phoneGroupFl10ContactList; //  maxOccurs="unbounded" minOccurs="0"

    @XStreamImplicit(itemFieldName = "email")
    final List<StringElement> emailList; //  maxOccurs="unbounded" minOccurs="0"

    public Fl10Contact() {
        phoneGroupFl10ContactList = null;

        emailList = null;
    }

    public Fl10Contact(List<PhoneGroupFl10Contact> phoneGroupFl10ContactList,  List<StringElement> emailList) {

        this.phoneGroupFl10ContactList = phoneGroupFl10ContactList == null || phoneGroupFl10ContactList.isEmpty()
                ? null : phoneGroupFl10ContactList;

        this.emailList = emailList == null || emailList.isEmpty() ? null : emailList;
    }

    public Fl10Contact(List<PhoneGroupFl10Contact> phoneGroupFl10ContactList, String email) {
        this.phoneGroupFl10ContactList = phoneGroupFl10ContactList;

        if(email == null) {
            emailList = null;
        } else {
            emailList = new ArrayList<>();

            emailList.add(new StringElement(email));
        }
    }

    public Fl10Contact(PhoneGroupFl10Contact phone, String email) {
        if(phone == null) {
            phoneGroupFl10ContactList = null;
        } else {
            phoneGroupFl10ContactList = new ArrayList<>();

            phoneGroupFl10ContactList.add(phone);
        }

        if(email == null) {
            emailList = null;
        } else {
            emailList = new ArrayList<>();

            emailList.add(new StringElement(email));
        }
    }

    public List<PhoneGroupFl10Contact> getPhoneGroupFl10ContactList() {
        return phoneGroupFl10ContactList;
    }

    public List<StringElement> getEmailList() {
        return emailList;
    }
}
