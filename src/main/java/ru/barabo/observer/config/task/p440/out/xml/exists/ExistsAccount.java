package ru.barabo.observer.config.task.p440.out.xml.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("��������")
public class ExistsAccount {

	@XStreamAlias("�����")
	private String code;

	@XStreamAlias("�����")
	private String type;

	@XStreamAlias("��������")
	private String opened;

	@XStreamAlias("��������")
	private String closed;

	@XStreamAlias("������")
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
