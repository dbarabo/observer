package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.ver4.request.RestRequestVer4;
import ru.barabo.observer.config.task.p440.load.ver4.request.ZsoFromFnsVer4;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class ZsnFromFns extends AbstractFromFns {

	@XStreamAlias("ЗАПНОНАЛИЧ")
	private ExistsRequest existsRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return existsRequest;
	}

	static public ZsnFromFns emptyZsnFromFns() {
		ZsnFromFns zsnFromFns = new ZsnFromFns();

		zsnFromFns.setTypeInfo("ЗАПНОНАЛИЧ");

		zsnFromFns.existsRequest = new ExistsRequest();

		zsnFromFns.existsRequest.setFns(new FnsXml());

		zsnFromFns.existsRequest.setBank( new BankXml(null, null));

		return zsnFromFns;
	}

}
