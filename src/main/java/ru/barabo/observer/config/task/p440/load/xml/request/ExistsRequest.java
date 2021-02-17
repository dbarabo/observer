package ru.barabo.observer.config.task.p440.load.xml.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.ver4.request.DatePeriod;
import ru.barabo.observer.config.task.p440.load.ver4.request.DateWorkState;

import java.sql.Date;

@XStreamAlias("ЗАПНОНАЛИЧ")
public final class ExistsRequest extends AbstractRequest {

	@XStreamAlias("ДатаПоСост")
	private String addDate;

	@XStreamAlias("ДейстПоСост")
	private DateWorkState dateWorkState;

	@XStreamAlias("ИмПериод")
	private DatePeriod datePeriod;

	@Override
	public Date getAddDate() {
		return getDateByState();
	}

	@Override
	public Date getSubDate() {
		return getDateEndByState();
	}

	private Date getDateByState() {
		if(addDate != null && (!addDate.isEmpty())) {
			return XmlLoader.parseDate(addDate);
		}

		Date onDate = dateWorkState == null ? null : dateWorkState.getOnDate();

		if(onDate != null) {
			return onDate;
		}

		return datePeriod.getStartDate();
	}

	private Date getDateEndByState() {
		if(addDate != null && (!addDate.isEmpty())) {
			return null;
		}

		Date onDate = dateWorkState == null ? null : dateWorkState.getOnDate();
		if(onDate != null) {
			return null;
		}

		return datePeriod.getEndDate();
	}

	@Override
	public String getAccounts() {
		return null;
	}
}
