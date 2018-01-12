package ru.barabo.observer.config.task.p440.load.xml.pno;


import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

public interface PnoData extends ParamsQuery {

	Bank getBenBank();

	String getBenAccount();

	PayerJur getBenName();

	String getQueuePay();

	String getCodePay();

	String getVidPor();

	String getUnkgnPay();

	String getKbkPay();

	String getOktmoPay();

	String getCodeOsnPay();

	String getSrokTrebPay();

	String getDateTrebPay();

	String getNumberTrebPay();
}
