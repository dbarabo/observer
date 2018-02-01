package ru.barabo.observer.config.task.p440.load.xml.decision;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.impl.Account;
import ru.barabo.observer.config.task.p440.load.xml.impl.Kesp;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

abstract public class AbstractDecision extends AbstractFromFnsInfo {

	final static transient protected Logger logger = Logger.getLogger(AbstractDecision.class
			.getName());

	@XStreamAlias("БИК")
	private String bankBik;

	@XStreamAlias("НаимБ")
	private String bankName;

	@XStreamAlias("ДатаРешПр")
	private String dateDecision;

	@XStreamAlias("НомРешПр")
	private String numberDecision;

	@XStreamAlias("КодОснов")
	private String mainCode;

	@XStreamImplicit(itemFieldName = "Счет")
	private List<Account> accounts;

	@XStreamImplicit(itemFieldName = "КЭСП")
	private List<Kesp> kesps;

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

	public Date getDateDecision() {
		return XmlLoader.parseDate(dateDecision);
	}

	public String getNumberDecision() {
		return numberDecision;
	}

	@Override
	public String getMainCode() {
		return mainCode;
	}

	@Override
	public String getAccounts() {

		if (accounts == null || accounts.size() == 0) {
			return null;
		}

		return accounts.stream().map(Account::getCode).collect(Collectors.joining(";"));
	}

	@Override
	public String getCards() {
		if (kesps == null || kesps.size() == 0) {
			return null;
		}
		return kesps.stream().map(Kesp::getIdKesp).collect(Collectors.joining(";"));
	}

	@Override
	public String getCardsCurrency() {
		if (kesps == null || kesps.size() == 0) {
			return null;
		}
		return kesps.stream().map(Kesp::getCurrency).collect(Collectors.joining(";"));
	}
}
