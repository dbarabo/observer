package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("����")
final public class ZsoFromFns extends AbstractFromFns {

	@XStreamAlias("����������")
	private RestRequest restRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {
		return restRequest;
	}

}
