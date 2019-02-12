package ru.barabo.observer.config.task.p440.load.xml.request;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionCancel;
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class ZsvFromFns extends AbstractFromFns {

	@XStreamAlias("ЗАПНОВЫПИС")
	private ExtractRequest extractRequest;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return extractRequest;
	}

	static public ZsvFromFns emptyZsvFromFns() {
		ZsvFromFns zsvFromFns = new ZsvFromFns();

		zsvFromFns.setTypeInfo("ЗАПНОВЫПИС");

		zsvFromFns.extractRequest = new ExtractRequest();

		zsvFromFns.extractRequest.setFns(new FnsXml());

		zsvFromFns.extractRequest.bank = new BankXml(null, null);

		return zsvFromFns;
	}

}
