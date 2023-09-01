package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XStreamAlias("ПФЛ")
public final class PayerPhysic implements ParamsQuery {

	@XStreamAlias("ИННФЛ")
	private String inn;

	@XStreamAlias("ДатаРожд")
	private String birthDay;

	@XStreamAlias("МестоРожд")
	private String birthPlace;

	@XStreamAlias("КодДок")
	private String codeDoc;

	@XStreamAlias("КодДУЛ")
	private String codeDocSaved;

	@XStreamAlias("СерНомДок")
	private String lineNumberDoc;

	@XStreamAlias("ДатаДок")
	private String dateDoc;

	@XStreamAlias("ФИО")
	private FioAttr fioSaved;

	@XStreamAlias("ФИОФЛ")
	private FioAttr fio;

	// @XStreamAlias("АдрПлат") old 2020
	@XStreamAlias("Адрес")
	private Address address;
	
	transient private Number idClient;
	
	public PayerPhysic() {
		
	}
	
	public PayerPhysic(Number idClient, String inn, String firstName, String lastName, 
			String secondName, String address, java.util.Date birhday, String birhPlace,
			String codeDoc,
			String lineNumberDoc, java.util.Date dateDoc) {
		
		this.idClient = idClient;
		this.inn = inn;

		this.fio = new FioAttr(firstName, lastName, secondName);

		this.address = Address.parseAddress(address);
		
		this.birthDay = XmlLoader.formatDate(birhday);

		this.birthPlace = birhPlace;

		this.codeDoc = codeDoc;

		this.lineNumberDoc = lineNumberDoc;

		this.dateDoc = XmlLoader.formatDate(dateDoc);
	}

	@Override
	public List<Object> getParams() {

		return new ArrayList<Object>(Arrays.asList(inn == null ? String.class : inn,
				fioSaved == null || fioSaved.getFirstName() == null ? String.class : fioSaved.getFirstName(),
				fioSaved == null || fioSaved.getLastName() == null ? String.class : fioSaved.getLastName(),
				fioSaved == null || fioSaved.getPapaName() == null ? String.class : fioSaved.getPapaName(),
				getBirthDay() == null ? Date.class : getBirthDay(),
				birthPlace == null ? String.class : birthPlace,
				codeDocSaved == null ? String.class : codeDocSaved,
				lineNumberDoc == null ? String.class : lineNumberDoc,
				getDateDoc() == null ? Date.class : getDateDoc(),
				address == null ? String.class : address.getAddress(),
				PayerType.Physic.getValueDb()));
	}

	private static final String COLUMNS = "INN, FIRST_NAME, LAST_NAME, SECOND_NAME, BIRHDAY, BIRTHPLACE, CODE_DOC, "
			+ "LINE_NUMBER_DOC, DATE_DOC, ADDRESS, TYPE, FNS_FROM, ID";

	@Override
	public String getListColumns() {

		return COLUMNS;
	}

	public String getInn() {
		return inn;
	}

	public Date getBirthDay() {
		return XmlLoader.parseDate(birthDay);
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public String getCodeDoc() {
		return codeDoc;
	}

	public String getLineNumberDoc() {
		return lineNumberDoc;
	}

	public Date getDateDoc() {
		return XmlLoader.parseDate(dateDoc);
	}

	public FioAttr getFio() {
		return fio;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
