package ru.barabo.observer.config.task.p440.load.xml;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;
import ru.barabo.observer.config.task.p440.load.xml.impl.FioAttr;

/**
 * представитель банка
 * 
 * @author debara
 *
 */
@XStreamAlias("ПредБанка")
public class SenderBank {

	@XStreamAlias("Должность")
	private String office;

	@XStreamAlias("ФИО")
	private FioAttr sender;

	public SenderBank(String office, String firstName, String lastName, String secondName) {
		this.office = office;

		sender = new FioAttr(firstName, lastName, secondName);
	}

	public static SenderBank OUR_MAIN_UOD = new SenderBank("Начальник Департамента обслуживания клиентов",
			"Лидия", "Юркова", "Васильевна");


	public static SenderBank create(String[] items) {
		if (items == null || items.length < 2) {
			return null;
		}

		return new SenderBank(items[0], items[1],
				items.length > 2 ? items[2] : null, items.length > 3 ? items[3] : null);
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public FioAttr getSender() {
		return sender;
	}

	public void setSender(FioAttr sender) {
		this.sender = sender;
	}
}
