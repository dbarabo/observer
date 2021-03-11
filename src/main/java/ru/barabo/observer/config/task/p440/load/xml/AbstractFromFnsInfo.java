package ru.barabo.observer.config.task.p440.load.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml;

/**
 * содержит общие для всех кроме PNO(там вообще маразм какой-то) атрибуты и теги
 * 
 * @author debara
 *
 */
abstract public class AbstractFromFnsInfo implements FromFnsInfo {

	@XStreamAlias("СвНО")
	protected FnsXml fns;

	@XStreamAlias("СвПл")
	protected PayerXml payer;

	@XStreamAlias("Руководитель")
	@XStreamOmitField
	protected Object ignoredElement;


	@Override
	public String getAccountsStartDates() {
		return "";
	}

	@Override
	public String getAccountsEndDates() {
		return "";
	}

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
