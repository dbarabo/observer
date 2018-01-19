package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("РеквПлат")
public class PayerInfo {

	@XStreamAlias("НаимПП")
	private String name;

	@XStreamAlias("ИННПП")
	private String inn;

	@XStreamAlias("КПППП")
	private String kpp;

	@XStreamAlias("НомСчПП")
	private String account;

	public PayerInfo(String name, String inn, String kpp, String account) {

		this.name = name;

		this.inn = inn;

		this.kpp = kpp;

		this.account = account;
	}
}
