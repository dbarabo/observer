package ru.barabo.observer.config.task.p440.load.xml.impl;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.barabo.observer.config.task.p440.load.xml.Bank;

@XStreamAlias("������")
public final class BankXml implements Bank {

	@XStreamAlias("���")
	private String bik;

	@XStreamAlias("�������")
	private String inn;

	@XStreamAlias("�������")
	private String kpp;

	@XStreamAlias("��������")
	private String name;

	@XStreamAlias("������")
	private String regNumber;

	@XStreamAlias("������")
	private String filial;
	
	private static class SingltonOurBank {

		private static final Object[] bankParam = {"040507717", "2540015598", "254001001",
				"�������� � ������������ ���������������� \"���������� ��������������� ������������ ����\"", "21"};


		private static final BankXml ourBank = new BankXml(bankParam/*Query.selectRow(Cfg.query440p().getOurBank()*/ );
	}
		
	static public BankXml ourBank() {
		return SingltonOurBank.ourBank;
	}
	
	private BankXml(Object[] row) {
		
		this.bik = (String)row[0];
		
		this.inn = (String)row[1];
		
		this.kpp = (String)row[2];
		
		this.name = (String)row[3];
		
		this.regNumber = (String)row[4];
		
		this.filial = "0";
	}


	public BankXml(String benBankBik, String benBankName) {
		this.bik = benBankBik;
		this.name = benBankName;
	}


	public String getBik() {
		return bik;
	}

	public String getInn() {
		return inn;
	}

	public String getKpp() {
		return kpp;
	}

	public String getName() {
		return name;
	}
}
