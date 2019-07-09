package ru.barabo.observer.config.task.p440.load.xml.apx;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionCancel;
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
public class ApzFromFns extends AbstractFromFns {

	@XStreamAlias("РЕШНОТЗПОР")
	private ApzInfo apz;

	@Override
	protected FromFnsInfo getFromFnsInfo() {

		return apz;
	}

	static public ApzFromFns emptyApzFromFns() {
		ApzFromFns apzFromFns = new ApzFromFns();

		apzFromFns.apz = new ApzInfo();

		apzFromFns.apz.setFns(new FnsXml());

		apzFromFns.setTypeInfo("РЕШНОТЗПОР");

		return apzFromFns;
	}
}

