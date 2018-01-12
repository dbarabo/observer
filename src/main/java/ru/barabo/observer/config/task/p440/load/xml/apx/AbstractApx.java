package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.sql.Date;
import java.util.List;

public abstract class AbstractApx extends AbstractFromFnsInfo {

	@XStreamAlias("НомРеш")
	private String mainNumber;

	@XStreamAlias("ДатаРеш")
	private String mainDate;

	@XStreamAlias("БанкПл")
	private String bankName;

	@XStreamAlias("БИКБПл")
	private String bankBik;

	@XStreamAlias("ИНННП")
	private String payerInn;

	@XStreamAlias("КППНП")
	private String payerKpp;

	@XStreamAlias("Сумма")
	private String sumPartRur;

	abstract protected List<PnoCancel> getPnoCancelList();

	private PnoCancel getPnoCancel() {
		if (getPnoCancelList() == null || getPnoCancelList().size() == 0) {
			return new PnoCancel();
		}

		return getPnoCancelList().get(0);
	}

	@Override
	public ParamsQuery getPayer() {

		return new PayerJur(payerInn, payerKpp, null);
	}

	@Override
	public Bank getBank() {
		return new Bank() {

			@Override
			public String getBik() {
				return bankBik;
			}

			@Override
			public String getName() {
				return bankName;
			}
		};
	}

	@Override
	public String getMainNumber() {
		return mainNumber;
	}

	@Override
	public Date getMainDate() {
		return XmlLoader.parseDate(mainDate);
	}

	@Override
	public String getMainCode() {
		return null;
	}

	@Override
	public String getMainDescription() {
		return getPnoCancel().getDescriptionFilePno();
	}

	@Override
	public Number getMainSum() {
		return sumPartRur == null
				? getPnoCancel().getMainSum()
				: XmlLoader.parseSum(sumPartRur).doubleValue() * 100;

	}

	@Override
	public String getMainType() {
		return getPnoCancel().getMainType();
	}

	@Override
	public String getMainStatus() {
		return null;
	}

	@Override
	public String getAccounts() {
		return getPnoCancel().getAccount();
	}

	@Override
	public String getCards() {
		return null;
	}

	@Override
	public String getCardsCurrency() {
		return null;
	}

	@Override
	public String getAddNumber() {
		return getPnoCancel().getAddNumberOldPno();
	}

	@Override
	public Date getAddDate() {
		return getPnoCancel().getAddDateOldPno();
	}

	@Override
	public String getSubNumber() {

		return null;
	}

	@Override
	public Date getSubDate() {
		return null;
	}
}
