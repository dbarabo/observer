package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("РеквБанка")
public class BankInfo {

	@XStreamAlias("НомКорСч")
	private String corrAccount;

	@XStreamAlias("НаимБП")
	private String name;

	@XStreamAlias("БИКБП")
	private String bik;

	public BankInfo(String corrAccount, String name, String bik) {

		this.corrAccount = corrAccount;

		this.name = name;

		this.bik = bik;
	}
}
