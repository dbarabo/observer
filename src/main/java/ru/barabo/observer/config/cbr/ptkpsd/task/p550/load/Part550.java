package ru.barabo.observer.config.cbr.ptkpsd.task.p550.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

abstract public class Part550 {

	@XStreamAlias("НомерЗаписи")
	private String numberRecord;

	@XStreamAlias("ФорматЗаписи")
	private String formatRecord;

	@XStreamAlias("ТипЗаписи")
	private String typeRecord;

	@XStreamAlias("ПризнакИнф")
	private String propertyInfo;

	@XStreamAlias("КодОтказа")
	private String codeError;

	@XStreamImplicit(itemFieldName = "КодПричинаОтказ")
	private List<String> codeReject;

	@XStreamAlias("ДатаОтказа")
	private String dateError;

	@XStreamAlias("ТипКлиента")
	private String typeClient;

	@XStreamAlias("ПризнакКлиента")
	private String propertyClient;

	@XStreamAlias("СведОперация")
	@XStreamOmitField
	protected Object operInfo;

	@XStreamAlias("Участник")
	@XStreamOmitField
	protected Object partner;

	@XStreamAlias("СтатусУчастника")
	private String statusPartner;

	@XStreamAlias("ТипУчастника")
	private String typePartner;

	@XStreamAlias("ПризнУчастника")
	private String propertyPartner;

	@XStreamAlias("СведЮЛ")
	@XStreamOmitField
	protected Object clientJuric;

	@XStreamAlias("СведФЛИП")
	@XStreamOmitField
	protected Object clientIp;

	@XStreamAlias("СведИНБОЮЛ")
	@XStreamOmitField
	protected Object clientIno;

	@XStreamAlias("ПричинаУдаления")
	private String propertyDeleted;

	public String getNumberRecord() {
		return numberRecord;
	}
}
