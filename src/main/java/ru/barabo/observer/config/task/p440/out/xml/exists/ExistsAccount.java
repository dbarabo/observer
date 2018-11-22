package ru.barabo.observer.config.task.p440.out.xml.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Сведения")
public class ExistsAccount {

	@XStreamAlias("НомСч")
	private String code;

	@XStreamAlias("ВидСч")
	private String type;

	@XStreamAlias("ДатаОткр")
	private String opened;

	@XStreamAlias("ДатаЗакр")
	private String closed;

	@XStreamAlias("КодВал")
	private String currency;

	public ExistsAccount(String code, String type, String currency, Date opened, Date closed) {

		this.code = code;
		this.type = type;

		this.currency = currency;

		this.opened = XmlLoader.formatDate(opened);

		this.closed = XmlLoader.formatDate(closed);
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getOpened() {
		return opened;
	}

	public String getClosed() {
		return closed;
	}

	public String getCurrency() {
		return currency;
	}
}
