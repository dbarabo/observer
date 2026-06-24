package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XStreamAlias("ПлИП")
public final class PayerIp implements ParamsQuery {

	@XStreamAlias("ИННИП")
	private String inn;

    @XStreamAlias("ИННФЛ")
    private String innFl;

	@XStreamAlias("ФИО")
	private FioAttr fio;

	transient private Number idClient;

	public PayerIp() {
	}

	public PayerIp(Number idClient, String inn, String firstName, String lastName, String secondName) {

		this.idClient = idClient;
		this.inn = inn;
		
		fio = new FioAttr(firstName, lastName, secondName);
	}

	public Number getIdClient() {
		return idClient;
	}

	public String getInn() {
		return inn == null ? innFl : inn;
	}

	public FioAttr getFio() {
		return fio;
	}

	@Override
	public List<Object> getParams() {

		return new ArrayList<Object>(Arrays.asList(getInn() == null ? String.class : getInn(),
				fio == null || fio.getFirstName() == null ? String.class : fio.getFirstName(),
				fio == null || fio.getLastName() == null ? String.class : fio.getLastName(),
				fio == null || fio.getPapaName() == null ? String.class : fio.getPapaName(),
                inn != null ? PayerType.Pboul.getValueDb() : PayerType.Physic.getValueDb() ));
	}

	private static final String COLUMNS = "INN, FIRST_NAME, LAST_NAME, SECOND_NAME, TYPE, FNS_FROM, ID";

	@Override
	public String getListColumns() {

		return COLUMNS;
	}
}
