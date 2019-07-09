package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class RpoFromFns extends AbstractFromFns {

	@XStreamAlias("РЕШНОПРИОС")
	private DecisionSuspend decisionSuspend;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return decisionSuspend;
	}


	static public RpoFromFns emptyRpoFromFns() {
		RpoFromFns rpoFromFns = new RpoFromFns();

		rpoFromFns.decisionSuspend = new DecisionSuspend();

		rpoFromFns.decisionSuspend.setFns(new FnsXml());

		rpoFromFns.setTypeInfo("РЕШНОПРИОС");

		return rpoFromFns;
	}
}

