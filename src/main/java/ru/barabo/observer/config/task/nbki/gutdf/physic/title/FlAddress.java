package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

public class FlAddress {

    @XStreamAlias("exist_0")
    private final StringElement exist0;

    @XStreamAlias("exist_1")
    private final StringElement exist1;

    @XStreamAlias("code")
    private final StringElement codeAdrReg;

    @XStreamAlias("postCode")
    private final StringElement postCode;

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
    private final StringElement estate; //8.10. Владение

    @XStreamAlias("block")
    private final StringElement block; //8.11. Корпус

    @XStreamAlias("build")
    private final StringElement build; //8.12. Строение

    @XStreamAlias("apart")
    private final StringElement apart; //8.13. Квартира

    @XStreamAlias("date")
    private final StringElement date; //8.14. Дата регистрации = -

    @XStreamAlias("dept")
    private final StringElement dept; //8.15. Наименование регистрирующего органа для паспорта

    @XStreamAlias("deptCode")
    private final StringElement deptCode; //8.16. Код подразделения, осуществившего регистрацию для паспорта

    public FlAddress(String postCode, String garAddr, String okato, String street,
                     String house, String estate, String build, String block, String apart, String dept, String deptCode) {

        exist0 = null;
        exist1 = null;

        this.codeAdrReg = new StringElement("1");

        this.postCode = postCode == null ? null : new StringElement(postCode);

        countryCode = new StringElement("643");

        this.garAddr = garAddr == null ? null : new StringElement(garAddr);

        this.okato = okato == null ? null : new StringElement(okato);

        this.street = street == null ? null : new StringElement(street);

        this.house = house == null ? null : new StringElement(house);

        this.estate = estate == null ? null : new StringElement(estate);

        this.block = block == null ? null : new StringElement(block);

        this.build = build == null ? null : new StringElement(build);

        this.apart = apart == null ? null : new StringElement(apart);

        date = new StringElement("-");

        this.dept = dept == null ? null : new StringElement(dept);

        this.deptCode = deptCode == null ? null : new StringElement(deptCode);
    }
}
