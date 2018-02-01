package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("����")
public class ApnFromFns extends AbstractFromFns {

	@XStreamAlias("����������")
	private ApnInfo apn;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return apn;
	}
}
