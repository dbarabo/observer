package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XStreamAlias("ПлЮЛ")
public final class PayerJur implements ParamsQuery {

	@XStreamAlias("ИННЮЛ")
	private String inn;

	@XStreamAlias("КПП")
	private String kpp;

	@XStreamAlias("НаимЮЛ")
	private String name;

	transient private Number idClient;

	public String getInn() {
		return inn;
	}

	public String getKpp() {
		return kpp;
	}

	public String getName() {
		return name;
	}

	public PayerJur() {
	};

	public PayerJur(String inn, String kpp, String name) {
		this.inn = inn;
		this.kpp = kpp;
		this.name = name;
	};

	public PayerJur(Number idClient, String nameJuric, String inn, String kpp) {

		this(inn, kpp, nameJuric);

		this.idClient = idClient;
	}

	@Override
	public List<Object> getParams() {

		return new ArrayList<Object>(Arrays.asList(inn == null ? String.class : inn,
				kpp == null ? String.class : kpp,
				name == null ? String.class : name,
				PayerType.Juric.getValueDb()));
	}

	private String COLUMNS = "INN, KPP, NAME, TYPE, FNS_FROM, ID";

	@Override
	public String getListColumns() {

		return COLUMNS;
	}
}
