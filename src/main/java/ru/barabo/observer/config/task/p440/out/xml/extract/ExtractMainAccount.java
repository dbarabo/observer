package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("��������")
public class ExtractMainAccount {

	@XStreamAlias("������")
	private String orderFile;

	@XStreamAlias("�����")
	private String code;

	@XStreamAlias("������")
	private String currency;

	@XStreamAlias("�������")
	private String startExtract;

	@XStreamAlias("�������")
	private String endExtract;

	@XStreamAlias("��������")
	private String startRest;

	@XStreamAlias("��������")
	private String debetAmount;

	@XStreamAlias("���������")
	private String creditAmount;

	@XStreamAlias("��������")
	private String endRest;

	public ExtractMainAccount(Integer orderFile, String account, String currency,
			Date startExtract, Date endExtract, Number startRest, Number endRest,
			Number debetAmount, Number creditAmount) {

		this.orderFile = String.format("%06d", orderFile);

		this.code = account;

		this.currency = currency;

		this.startExtract = XmlLoader.formatDate(startExtract);

		this.endExtract = XmlLoader.formatDate(endExtract);

		this.endExtract = XmlLoader.formatDate(endExtract);

		this.startRest = XmlLoader.formatSum(startRest);

		this.endRest = XmlLoader.formatSum(endRest);

		this.endRest = XmlLoader.formatSum(endRest);

		this.debetAmount = XmlLoader.formatSum(debetAmount);

		this.creditAmount = XmlLoader.formatSum(creditAmount);
	}

	public String getCode() {
		return code;
	}

	public java.sql.Date getStartExtract() {

		return XmlLoader.parseDate(startExtract);
	}

	public java.sql.Date getEndExtract() {

		return XmlLoader.parseDate(endExtract);
	}
}
