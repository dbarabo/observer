package ru.barabo.observer.config.task.p440.load.xml.apx;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("����")
public class ApzFromFns extends AbstractFromFns {

	@XStreamAlias("����������")
	private ApzInfo apz;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return apz;
	}
}

