package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("ОстОборотСЧ")
public class TurnExtractAccount {

    @XStreamAlias("ОстВх")
    private String startRest;

    @XStreamAlias("СумДеб")
    private String debetAmount;

    @XStreamAlias("СумКред")
    private String creditAmount;

    @XStreamAlias("ОстИсх")
    private String endRest;

    public TurnExtractAccount(Number startRest, Number endRest, Number debet, Number credit) {

        this.startRest = XmlLoader.formatSum(startRest);

        this.debetAmount = XmlLoader.formatSum(debet);

        this.creditAmount = XmlLoader.formatSum(credit);

        this.endRest = XmlLoader.formatSum(endRest);
    }
}
