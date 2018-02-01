package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("���������")
public class BankInfo {

	@XStreamAlias("��������")
	private String corrAccount;

	@XStreamAlias("������")
	private String name;

	@XStreamAlias("�����")
	private String bik;

	public BankInfo(String corrAccount, String name, String bik) {

		this.corrAccount = corrAccount;

		this.name = name;

		this.bik = bik;
	}
}
