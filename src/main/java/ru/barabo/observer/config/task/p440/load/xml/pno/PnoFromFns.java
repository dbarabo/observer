package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;

@XStreamAlias("����")
final public class PnoFromFns extends AbstractFromFns {

	@XStreamAlias("����������")
	private OrderTax orderTax;

	@Override
	public FromFnsInfo getFromFnsInfo() {

		return orderTax;
	}
}