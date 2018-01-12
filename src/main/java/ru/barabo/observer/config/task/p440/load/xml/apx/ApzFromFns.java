package ru.barabo.observer.config.task.p440.load.xml.apx;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("Файл")
public class ApzFromFns extends AbstractFromFns {

	@XStreamAlias("РЕШНОТЗПОР")
	private ApzInfo apz;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return apz;
	}
}

