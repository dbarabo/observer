package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Сведения")
public class ExtractMainAccount {

	@XStreamAlias("ПорНом")
	private String orderFile;

	@XStreamAlias("НомСч")
	private String code;

	@XStreamAlias("КодВал")
	private String currency;

	@XStreamAlias("ДатаНач")
	private String startExtract;

	@XStreamAlias("ДатаКон")
	private String endExtract;

	@XStreamAlias("ОстатНач")
	private String startRest;

	@XStreamAlias("СуммаДеб")
	private String debetAmount;

	@XStreamAlias("СуммаКред")
	private String creditAmount;

	@XStreamAlias("ОстатКон")
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
