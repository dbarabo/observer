package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ĞÅØÍÎÏĞÈÎÑ")
final public class DecisionSuspend extends AbstractDecision {

	@XStreamAlias("ÑóìÂçûñê")
	private String mainSum;

	@XStreamAlias("ÍîìĞåøÂçûñê")
	private String addNumber;

	@XStreamAlias("ÄàòàĞåøÂçûñê")
	private String addDate;

	@XStreamAlias("Îáñòîÿò")
	private String mainDescription;

	@Override
	public String getMainNumber() {

		return getNumberDecision();
	}

	@Override
	public Date getMainDate() {

		return getDateDecision();
	}

	@Override
	public String getMainDescription() {

		return mainDescription;
	}

	@Override
	public Number getMainSum() {

		return XmlLoader.parseSum(mainSum);
	}

	@Override
	public String getMainType() {
		return null;
	}

	@Override
	public String getMainStatus() {
		return null;
	}

	@Override
	public String getAddNumber() {
		return addNumber;
	}

	@Override
	public Date getAddDate() {

		return XmlLoader.parseDate(addDate);
	}

	@Override
	public String getSubNumber() {
		return null;
	}

	@Override
	public Date getSubDate() {
		return null;
	}

}
