package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("Файл")
public class ApoFromFns extends AbstractFromFns {

	@XStreamAlias("РЕШНООТПОР")
	private ApoInfo apo;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return apo;
	}
}
