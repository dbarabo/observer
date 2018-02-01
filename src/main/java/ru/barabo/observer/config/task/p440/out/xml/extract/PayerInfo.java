package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("��������")
public class PayerInfo {

	@XStreamAlias("������")
	private String name;

	@XStreamAlias("�����")
	private String inn;

	@XStreamAlias("�����")
	private String kpp;

	@XStreamAlias("�������")
	private String account;

	public PayerInfo(String name, String inn, String kpp, String account) {

		this.name = name;

		this.inn = inn;

		this.kpp = kpp;

		this.account = account;
	}
}
