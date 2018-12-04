package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class RooFromFns extends AbstractFromFns {


    @XStreamAlias("РЕШНООТМЕН")
	private DecisionCancel decisionCancel;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return decisionCancel;
	}

    public void setDecisionCancel(DecisionCancel decisionCancel) {
        this.decisionCancel = decisionCancel;
    }

    public DecisionCancel getDecisionCancel() {
        return decisionCancel;
    }

    static public RooFromFns EmptyRooFromFns() {
        RooFromFns rooFromFns = new RooFromFns();

        rooFromFns.decisionCancel = new DecisionCancel();

        rooFromFns.decisionCancel.setFns(new FnsXml());

        rooFromFns.setTypeInfo("РЕШНООТМЕН");

        return rooFromFns;
    }
}
