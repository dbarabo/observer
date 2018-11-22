package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("ПриостПоруч")
public class PnoCancel {

	@XStreamAlias("НомПоруч")
	private String addNumberOldPno;

	@XStreamAlias("ДатаПоруч")
	private String addDateOldPno;

	@XStreamAlias("ВидПоруч")
	private String mainType;

	@XStreamAlias("НомСчПл")
	private String account;


	@XStreamAlias("ВидСч")
	private String typeAccount;

	@XStreamAlias("НаимФПоруч")
	private String descriptionFilePno;

	@XStreamAlias("СумПлат")
	private String mainSum;

	public String getAddNumberOldPno() {
		return addNumberOldPno;
	}

	public Date getAddDateOldPno() {
		return XmlLoader.parseDate(addDateOldPno);
	}

	public String getMainType() {
		return mainType;
	}

	public String getAccount() {
		return account;
	}

	public String getDescriptionFilePno() {
		return descriptionFilePno;
	}

	public Number getMainSum() {
		return XmlLoader.parseSum(mainSum);
	}
}
