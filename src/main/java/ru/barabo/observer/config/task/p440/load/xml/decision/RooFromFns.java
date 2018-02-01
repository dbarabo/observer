package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("����")
final public class RooFromFns extends AbstractFromFns {

	@XStreamAlias("����������")
	private DecisionCancel decisionCancel;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return decisionCancel;
	}
}
