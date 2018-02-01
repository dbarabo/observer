package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.sql.Date;

@XStreamAlias("�����������")
public class PnoCancel {

	@XStreamAlias("��������")
	private String addNumberOldPno;

	@XStreamAlias("���������")
	private String addDateOldPno;

	@XStreamAlias("��������")
	private String mainType;

	@XStreamAlias("�������")
	private String account;


	@XStreamAlias("�����")
	private String typeAccount;

	@XStreamAlias("����������")
	private String descriptionFilePno;

	@XStreamAlias("�������")
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
