package ru.barabo.observer.config.task.p440.load.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml;

/**
 * содержит общие дл€ всех кроме PNO(там вообще маразм какой-то) атрибуты и теги
 * 
 * @author debara
 *
 */
abstract public class AbstractFromFnsInfo implements FromFnsInfo {

	@XStreamAlias("—вЌќ")
	protected FnsXml fns;

	@XStreamAlias("—вѕл")
	protected PayerXml payer;

	@XStreamAlias("–уководитель")
	@XStreamOmitField
	protected Object ignoredElement;

	public FnsXml getFns() {
		return fns;
	}

	public void setFns(FnsXml fns) {
		this.fns = fns;
	}

	public ParamsQuery getPayer() {
		return payer;
	}

	public void setPayer(PayerXml payer) {
		this.payer = payer;
	}
}
