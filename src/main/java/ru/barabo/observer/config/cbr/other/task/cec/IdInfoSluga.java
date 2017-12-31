package ru.barabo.observer.config.cbr.other.task.cec;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ИДИнфо")
public class IdInfoSluga {

	transient static private final Logger logger = Logger.getLogger(IdInfoSluga.class.getName());

	@XStreamAlias("КодСубъекта")
	@XStreamAsAttribute
	private String code; // КодСубъекта
	
	@XStreamAlias("Кампания")
	@XStreamAsAttribute
	private String company; //Кампания
	
	@XStreamAlias("Система")
	@XStreamAsAttribute
	private Integer systema; // Система

	public Integer getCode() {

		if (code == null) {
			return null;
		}

		Integer intCode = null;

		try {
			intCode = Integer.parseInt(code.trim());
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException IdInfoSluga.getCode code=" + code, e);
		}

		return intCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getSystema() {
		return systema;
	}

	public void setSystema(Integer systema) {
		this.systema = systema;
	}
}
