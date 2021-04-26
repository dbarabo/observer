package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;

import java.util.Date;
import java.util.List;

@XStreamAlias("СвПл")
public final class PayerXml implements ParamsQuery {

	@XStreamAlias("ПлЮЛ")
	private PayerJur payerJur;

	@XStreamAlias("ПлИП")
	private PayerIp payerIp;

	@XStreamAlias("ПФЛ")
	private PayerPhysic payerPhysic;

	@XStreamAlias("АдрПлат")
	private Address address;

	@Override
	public List<Object> getParams() {

		if(payerJur != null) {
			return payerJur.getParams();
		}

		if (payerIp != null) {
			return payerIp.getParams();
		}

		if (payerPhysic != null) {
			return payerPhysic.getParams();
		}

		return null;
	}

	@Override
	public String getListColumns() {

		if (payerJur != null) {
			return payerJur.getListColumns();
		}

		if (payerIp != null) {
			return payerIp.getListColumns();
		}

		if (payerPhysic != null) {
			return payerPhysic.getListColumns();
		}

		return null;
	}
	
	public PayerJur getPayerJur() {
		return payerJur;
	}

	public PayerIp getPayerIp() {
		return payerIp;
	}

	public PayerPhysic getPayerPhysic() {
		return payerPhysic;
	}

	static public PayerXml createJuric(Number idClient, String nameJuric, String inn, String kpp) {
		PayerXml result = new PayerXml();
		
		result.payerJur = new PayerJur(idClient, nameJuric, inn, kpp);
		
		return result;
	}
	
	static public PayerXml createPboul(Number idClient, String inn, String firstName,
			String lastName, String secondName) {
		PayerXml result = new PayerXml();

		result.payerIp = new PayerIp(idClient, inn, firstName, lastName, secondName);

		return result;
	}
	
	/**
	 * краткий формат Физика - для отправки в ИФНС
	 */
	static public PayerXml createPhysicShort(Number idClient, String inn, String firstName,
			String lastName, String secondName, String address, Date birhday, String birhPlace,
			String codeDoc, String lineNumberDoc, Date dateDoc) {

		PayerXml result = new PayerXml();

		result.payerPhysic = new PayerPhysic(idClient, inn, firstName, lastName, secondName,
				null, null, null, null, null, null);

		return result;
	}

	static public PayerXml createPhysic(Number idClient, String inn, String firstName,
			String lastName, String secondName, String address, Date birhday, String birhPlace,
			String codeDoc, String lineNumberDoc, Date dateDoc) {
		PayerXml result = new PayerXml();

		result.payerPhysic = new PayerPhysic(idClient, inn, firstName, lastName, secondName,
				address, birhday, birhPlace, codeDoc, lineNumberDoc, dateDoc);


		return result;
	}
}
