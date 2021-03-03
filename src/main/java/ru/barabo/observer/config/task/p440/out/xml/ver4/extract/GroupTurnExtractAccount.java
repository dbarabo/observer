package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("СвОстОборотСЧ")
public class GroupTurnExtractAccount {

    @XStreamImplicit(itemFieldName = "ОстОборотСЧ")
    private List<TurnExtractAccount> turnExtractAccounts;

    public GroupTurnExtractAccount(Number startRest, Number endRest, Number debet, Number credit) {

        turnExtractAccounts = new ArrayList<TurnExtractAccount>();

        turnExtractAccounts.add( new TurnExtractAccount(startRest, endRest, debet, credit) );
    }
}
