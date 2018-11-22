package ru.barabo.observer.config.task.p440.load.xml.impl;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.barabo.observer.config.task.p440.load.xml.Bank;

@XStreamAlias("СвБанк")
public final class BankXml implements Bank {

	@XStreamAlias("БИК")
	private String bik;

	@XStreamAlias("ИННБанк")
	private String inn;

	@XStreamAlias("КППБанк")
	private String kpp;

	@XStreamAlias("НаимБанк")
	private String name;

	@XStreamAlias("РегНом")
	private String regNumber;

	@XStreamAlias("НомФил")
	private String filial;
	
	private static class SingltonOurBank {

		private static final Object[] bankParam = {"040507717", "2540015598", "254001001",
				"Общество с ограниченной ответственностью \"Приморский территориальный коммерческий банк\"", "21"};


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
