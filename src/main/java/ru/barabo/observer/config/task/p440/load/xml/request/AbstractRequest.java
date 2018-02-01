package ru.barabo.observer.config.task.p440.load.xml.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

import java.sql.Date;

abstract public class AbstractRequest extends AbstractFromFnsInfo {

	@XStreamAlias("�������")
	protected String mainNumber;

	@XStreamAlias("��������")
	protected String mainDate;

	@XStreamAlias("��������")
	protected String mainCode;

	@XStreamAlias("�������")
	protected String mainType;

	@XStreamAlias("���������")
	protected String mainDescription;

	@XStreamAlias("�������")
	protected String mainStatus;

	@XStreamAlias("������")
	protected BankXml bank;

	@Override
	public Bank getBank() {

		return bank;
	}

	@Override
	public String getMainNumber() {
		return mainNumber;
	}

	public void setMainNumber(String mainNumber) {
		this.mainNumber = mainNumber;
	}

	@Override
	public Date getMainDate() {
		return XmlLoader.parseDate(mainDate);
	}

	public void setMainDate(String mainDate) {
		this.mainDate = mainDate;
	}

	@Override
	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}

	@Override
	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	@Override
	public String getMainDescription() {
		return mainDescription;
	}

	public void setMainDescription(String mainDescription) {
		this.mainDescription = mainDescription;
	}

	@Override
	public String getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(String mainStatus) {
		this.mainStatus = mainStatus;
	}

	@Override
	public Number getMainSum() {
		return null;
	}

	@Override
	public String getCards() {
		return null;
	}

	@Override
	public String getCardsCurrency() {
		return null;
	}

	@Override
	public String getAddNumber() {
		return null;
	}

	@Override
	public String getSubNumber() {
		return null;
	}
}
