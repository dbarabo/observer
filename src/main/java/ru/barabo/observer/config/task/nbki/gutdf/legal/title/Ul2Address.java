package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.PhoneGroupFl10Contact;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.List;

/**
 * Блок 2. Адрес юридического лица в пределах его места нахождения и контактная информация
 */
public class Ul2Address {

    @XStreamAlias("countryCode")
    private final StringElement countryCode;// = new StringElement("643");

    @XStreamAlias("regStateNum")
    private final StringElement garAddr; // может фиас пока подойдет?

    @XStreamAlias("locationCode")
    private final StringElement okato;

    @XStreamAlias("street")
    private final StringElement street;

    @XStreamAlias("house")
    private final StringElement house;

    @XStreamAlias("estate")
    private final StringElement estate; // Владение

    @XStreamAlias("block")
    private final StringElement block; // Корпус

    @XStreamAlias("build")
    private final StringElement build; // Строение

    @XStreamAlias("apart")
    private final StringElement apart; //Квартира

    @XStreamImplicit(itemFieldName = "Phone_group_UL_2_Address")
    private final List<PhoneGroupFl10Contact> phoneList; // minOccurs="0"

    @XStreamImplicit(itemFieldName = "email")
    final List<StringElement> emailList; //  maxOccurs="unbounded" minOccurs="0"

    @XStreamAlias("foreignerFact_0")
    private final StringElement foreignerFact0; //2.15. Признак иностранного юридического лица = 0

    @XStreamAlias("foreignerFact_1")
    private final StringElement foreignerFact1; //2.15. Признак иностранного юридического лица = 1

    public Ul2Address(String garAddr, String okato, String street,
                     String house, String estate, String build, String block, String apart,
                      List<PhoneGroupFl10Contact> phoneList, List<StringElement> emailList) {

         countryCode = new StringElement("643");

        this.garAddr = garAddr == null ? null : new StringElement(garAddr);

        this.okato = okato == null ? null : new StringElement(okato);

        this.street = street == null ? null : new StringElement(street);

        this.house = house == null ? null : new StringElement(house);

        this.estate = estate == null ? null : new StringElement(estate);

        this.block = block == null ? null : new StringElement(block);

        this.build = build == null ? null : new StringElement(build);

        this.apart = apart == null ? null : new StringElement(apart);

        this.phoneList = phoneList;

        this.emailList = emailList;

        foreignerFact0 = new StringElement("");
        foreignerFact1 = null;
    }

    public StringElement getGarAddr() {
        return garAddr;
    }

    public StringElement getOkato() {
        return okato;
    }

    public StringElement getStreet() {
        return street;
    }

    public StringElement getHouse() {
        return house;
    }

    public StringElement getEstate() {
        return estate;
    }

    public StringElement getBlock() {
        return block;
    }

    public StringElement getBuild() {
        return build;
    }

    public StringElement getApart() {
        return apart;
    }

    public List<PhoneGroupFl10Contact> getPhoneList() {
        return phoneList;
    }

    public List<StringElement> getEmailList() {
        return emailList;
    }
}
