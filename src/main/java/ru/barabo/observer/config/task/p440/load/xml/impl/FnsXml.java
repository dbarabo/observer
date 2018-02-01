package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.Fns;

@XStreamAlias("—‚ÕŒ")
public final class FnsXml implements Fns {

	@XStreamAlias(" Ó‰ÕŒ")
	private String fnsCode;

	@XStreamAlias("Õ‡ËÏÕŒ")
	private String nameFns;

	public FnsXml() {
	}

	public FnsXml(String fnsCode, String nameFns) {

		this.fnsCode = fnsCode;

		this.nameFns = nameFns;
	}

	public String getNameFns() {
		return nameFns;
	}

	public String getFnsCode() {
		return fnsCode;
	}
}
