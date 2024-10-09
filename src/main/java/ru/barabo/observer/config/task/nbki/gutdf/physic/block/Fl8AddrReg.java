package ru.barabo.observer.config.task.nbki.gutdf.physic.block;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class Fl8AddrReg {

    @XStreamAlias("code")
    private final StringElement code; // 8.1. Код адреса регистрации 1-по месту жительства |emptyValueType

    @XStreamAlias("postCode")
    private final StringElement postCode; // 8.2. Почтовый индекс

    @XStreamAlias("countryCode")
    private final StringElement countryCode; // 8.3. Код страны по ОКСМ 643

    @XStreamAlias("regStateNum")
    private final StringElement regStateNum; // 8.5. Номер адреса в ГАР minOccurs="0"

    @XStreamAlias("locationCode")
    private final StringElement okato; // 8.6. Код населенного пункта по ОКАТО

    @XStreamAlias("street")
    private final StringElement street;

    @XStreamAlias("house")
    private final StringElement house; // 8.9. Дом

    @XStreamAlias("estate")
    private final StringElement estate; // 8.10. Владение

    @XStreamAlias("block")
    private final StringElement block; // 8.11. Корпус

    @XStreamAlias("build")
    private final StringElement build; // 8.12. Строение

    @XStreamAlias("apart")
    private final StringElement apart; // 8.13. Квартира

    @XStreamAlias("date")
    private final StringElement date; // 8.14. Дата регистрации  |emptyValueType

    public Fl8AddrReg(Integer code, String postCode, String regStateNum, String okato, String street, String house,
                      String estate, String block, String build, String apart) {

        this.code = new StringElement(code == null ? "1" : code.toString());

        this.postCode = postCode == null || postCode.isEmpty() ? null : new StringElement(postCode);

        this.countryCode = new StringElement("643");

        this.regStateNum = regStateNum == null ? null : new StringElement(regStateNum);

        this.okato = okato == null || okato.isEmpty() ? null : new StringElement(okato);

        this.street = street == null ? null : new StringElement(street);

        this.house = house == null ? null : new StringElement(house);

        this.estate = estate == null ? null : new StringElement(estate);

        this.block = block == null ? null : new StringElement(block);

        this.build = build == null ? null : new StringElement(build);

        this.apart = apart == null ? null : new StringElement(apart);

        this.date = new StringElement("-");
    }
}
