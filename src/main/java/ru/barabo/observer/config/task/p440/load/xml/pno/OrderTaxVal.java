package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("������")
public final class OrderTaxVal {

	@XStreamAlias("����������")
	private String datePorVal;

	@XStreamAlias("��������")
	private String accountPorVal;

	@XStreamAlias("���������")
	private String numberPorVal;

	public java.sql.Date getDatePorVal() {
		return XmlLoader.parseDate(datePorVal);
	}

	public String getAccountPorVal() {
		return accountPorVal;
	}

	public String getNumberPorVal() {
		return numberPorVal;
	}
}