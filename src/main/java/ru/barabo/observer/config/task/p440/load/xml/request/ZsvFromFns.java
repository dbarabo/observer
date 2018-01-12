package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("Файл")
final public class ZsvFromFns extends AbstractFromFns {

	@XStreamAlias("ЗАПНОВЫПИС")
	private ExtractRequest extractRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return extractRequest;
	}

}
