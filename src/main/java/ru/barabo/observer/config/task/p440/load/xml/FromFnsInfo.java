package ru.barabo.observer.config.task.p440.load.xml;

import java.sql.Date;

public interface FromFnsInfo {

	Bank getBank();

	Fns getFns();

	ParamsQuery getPayer();

	String getMainNumber();
	
	Date getMainDate();
	
	String getMainCode();

	String getMainDescription();

	Number getMainSum();

	String getMainType();

	String getMainStatus();

	String getAccounts();

	String getCards();

	String getCardsCurrency();

	String getAddNumber();

	Date getAddDate();

	String getSubNumber();

	Date getSubDate();

	String getAccountsStartDates();

	String getAccountsEndDates();

	String getUID();
}
