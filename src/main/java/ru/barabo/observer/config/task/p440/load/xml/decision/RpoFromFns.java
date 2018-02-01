package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("тЮИК")
final public class RpoFromFns extends AbstractFromFns {

	@XStreamAlias("пеьмнопхня")
	private DecisionSuspend decisionSuspend;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return decisionSuspend;
	}
}

