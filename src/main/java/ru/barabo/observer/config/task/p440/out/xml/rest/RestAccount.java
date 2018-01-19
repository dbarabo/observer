package ru.barabo.observer.config.task.p440.out.xml.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Остатки")
public class RestAccount {

	@XStreamAlias("НомСч")
	private String code;

	@XStreamAlias("ВидСч")
	private String type;

	@XStreamAlias("КодВал")
	private String currency;

	@XStreamAlias("Остаток")
	private String rest;

	public String getCode() {
		return code;
	}

	public RestAccount() {
	}

	public RestAccount(String code, String type, String currency, Number rest) {

		this.code = code;
		this.type = type;

		this.currency = currency;

		this.rest = XmlLoader.formatSum(rest);
	}
}
