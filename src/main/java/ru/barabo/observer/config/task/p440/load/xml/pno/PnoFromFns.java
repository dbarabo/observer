package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionCancel;
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;

@XStreamAlias("Файл")
final public class PnoFromFns extends AbstractFromFns {

	@XStreamAlias("ПОРУЧСЧЕТН")
	private OrderTax orderTax;

	@Override
	public FromFnsInfo getFromFnsInfo() {

		return orderTax;
	}

	static public PnoFromFns EmptyFromFns() {
		PnoFromFns pnoFromFns = new PnoFromFns();

		pnoFromFns.orderTax = new OrderTax();

        pnoFromFns.setTypeInfo("ПОРУЧСЧЕТН");

		return pnoFromFns;
	}
}