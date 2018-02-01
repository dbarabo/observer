package ru.barabo.observer.config.task.p440.load.xml.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ÇÀÏÍÎÍÀËÈ×")
public final class ExistsRequest extends AbstractRequest {

	@XStreamAlias("ÄàòàÏîÑîñò")
	private String addDate;

	@Override
	public Date getAddDate() {
		return XmlLoader.parseDate(addDate);
	}

	@Override
	public String getAccounts() {
		return null;
	}

	@Override
	public Date getSubDate() {
		return null;
	}

}
