package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class ZsoFromFns extends AbstractFromFns {

	@XStreamAlias("ЗАПНООСТАТ")
	private RestRequest restRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {
		return restRequest;
	}

	static public ZsoFromFns emptyZsoFromFns() {
		ZsoFromFns zsoFromFns = new ZsoFromFns();

		zsoFromFns.setTypeInfo("ЗАПНООСТАТ");

		zsoFromFns.restRequest = new RestRequest();

		zsoFromFns.restRequest.setFns(new FnsXml());

		zsoFromFns.restRequest.bank = new BankXml(null, null);

		return zsoFromFns;
	}

	static public ZsoFromFns emptyZsoFromFnsErrorTypeFormat(String typeFormat) {
		ZsoFromFns zsoFromFns = new ZsoFromFns();

		zsoFromFns.setTypeInfo(typeFormat);

		zsoFromFns.restRequest = new RestRequest();

		zsoFromFns.restRequest.setFns(new FnsXml());

		zsoFromFns.restRequest.bank = new BankXml(null, null);

		return zsoFromFns;
	}

}
