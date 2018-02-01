package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("����������")
final public class DecisionCancel extends AbstractDecision {

	@XStreamAlias("���������")
	private String cancelDate;

	@XStreamAlias("��������")
	private String cancelNumber;

	@XStreamAlias("��������")
	private String subNumber;

	@XStreamAlias("���������")
	private String subDate;

	@XStreamAlias("������")
	private String mainType;

	@Override
	public String getMainNumber() {

		return cancelNumber;
	}

	@Override
	public Date getMainDate() {

		return XmlLoader.parseDate(cancelDate);
	}

	@Override
	public String getMainDescription() {

		return null;
	}

	@Override
	public Number getMainSum() {

		return null;
	}

	@Override
	public String getMainType() {

		return mainType;
	}

	@Override
	public String getMainStatus() {

		return null;
	}

	@Override
	public String getAddNumber() {

		return getNumberDecision();
	}

	@Override
	public Date getAddDate() {

		return getDateDecision();
	}

	@Override
	public String getSubNumber() {

		return subNumber;
	}

	@Override
	public Date getSubDate() {

		return XmlLoader.parseDate(subDate);
	}
}
