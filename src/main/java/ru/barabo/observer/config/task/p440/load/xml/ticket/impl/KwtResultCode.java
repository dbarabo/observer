package ru.barabo.observer.config.task.p440.load.xml.ticket.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("���������")
public class KwtResultCode {

	@XStreamAlias("��������������")
	private String codeResult;

	@XStreamAlias("���������")
	private String errorDescription;

	@XStreamAlias("�������")
	private String errorAttribute;

	@XStreamAlias("��������")
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
