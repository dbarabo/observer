package ru.barabo.observer.config.cbr.ptkpsd.task.p550.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СлужЧасть")
public class ServicePart {

	@XStreamAlias("ДатаСообщения")
	protected String dateMessage;

	@XStreamAlias("ВерсияФормата")
	protected String versInfo;

	@XStreamAlias("ТелОператор")
	protected String phone;

	@XStreamAlias("ЭлектроннаяПочта")
	protected String mail;

	public String getDateMessage() {
		return dateMessage;
	}
}
