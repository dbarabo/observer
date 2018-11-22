package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("Файл")
final public class ZsnFromFns extends AbstractFromFns {

	@XStreamAlias("ЗАПНОНАЛИЧ")
	private ExistsRequest existsRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return existsRequest;
	}

}
