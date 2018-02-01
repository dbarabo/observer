package ru.barabo.observer.config.task.p440.load.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import ru.barabo.observer.config.task.p440.load.xml.impl.FnsXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml;

/**
 * �������� ����� ��� ���� ����� PNO(��� ������ ������ �����-��) �������� � ����
 * 
 * @author debara
 *
 */
abstract public class AbstractFromFnsInfo implements FromFnsInfo {

	@XStreamAlias("����")
	protected FnsXml fns;

	@XStreamAlias("����")
	protected PayerXml payer;

	@XStreamAlias("������������")
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
