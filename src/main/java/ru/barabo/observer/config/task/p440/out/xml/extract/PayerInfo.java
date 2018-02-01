package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ĞåêâÏëàò")
public class PayerInfo {

	@XStreamAlias("ÍàèìÏÏ")
	private String name;

	@XStreamAlias("ÈÍÍÏÏ")
	private String inn;

	@XStreamAlias("ÊÏÏÏÏ")
	private String kpp;

	@XStreamAlias("ÍîìÑ÷ÏÏ")
	private String account;

	public PayerInfo(String name, String inn, String kpp, String account) {

		this.name = name;

		this.inn = inn;

		this.kpp = kpp;

		this.account = account;
	}
}
