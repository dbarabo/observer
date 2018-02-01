package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Результат")
public class KwtResultCode {

	@XStreamAlias("КодРезПроверки")
	private String codeResult;

	@XStreamAlias("Пояснение")
	private String errorDescription;

	@XStreamAlias("КодРекв")
	private String errorAttribute;

	@XStreamAlias("ЗначРекв")
	private String errorValue;

	public String getCodeResult() {
		return codeResult;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getErrorAttribute() {
		return errorAttribute;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public KwtResultCode(String codeResult) {
		this.codeResult = codeResult;
	}
}
