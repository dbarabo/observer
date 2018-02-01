package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.Fns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.sql.Date;

@XStreamAlias("����������")
public final class OrderTax implements FromFnsInfo {

	@XStreamAlias("������")
	private String bankBik;

	@XStreamAlias("������")
	private String bankName;

	@XStreamAlias("���������")
	private String mainDate;

	@XStreamAlias("��������")
	private String addDate;

	@XStreamAlias("�����")
	private String payerInn;

	@XStreamAlias("���")
	private String kbkPay;

	@XStreamAlias("�����")
	private String payerKpp;

	@XStreamAlias("������")
	private String mainDescription;

	@XStreamAlias("��������")
	private String mainNumber;

	@XStreamAlias("�������")
	private String accounts;

	@XStreamAlias("�������")
	private String addNumber;

	@XStreamAlias("�����")
	private String oktmoPay;

	@XStreamAlias("������")
	private String queuePay;

	@XStreamAlias("��������")
	private String payerName;

	@XStreamAlias("�������")
	private Integer mainSumKopeika;

	@XStreamAlias("������")
	private String mainStatus;

	@XStreamAlias("�����")
	private String mainType;

	@XStreamAlias("������")
	private String vidPorPay;

	@XStreamAlias("�����")
	private String codePay;

	@XStreamAlias("������")
	private String codeOsnPay;

	@XStreamAlias("���������")
	private String srokTrebPay;

	@XStreamAlias("�������")
	private String benBankBik;

	@XStreamAlias("�������")
	private String benBankName;

	@XStreamAlias("������")
	private String benInn;

	@XStreamAlias("������")
	private String benKpp;

	@XStreamAlias("�����")
	private String benName;

	@XStreamAlias("��������")
	private String benAccount;

	@XStreamAlias("�����")
	private String unkgn;

	@XStreamAlias("������")
	private OrderTaxVal orderTaxVal;

	public ParamsQuery getPnoData() {
		return new PnoDataImpl(benBankBik, benBankName, benAccount, benInn, benKpp,
				benName, queuePay, codePay, vidPorPay, unkgn, kbkPay, oktmoPay, codeOsnPay, 
				srokTrebPay, addDate, addNumber, orderTaxVal);
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
	public Fns getFns() {
		return new Fns() {

			@Override
			public String getNameFns() {
				return null;
			}

			@Override
			public String getFnsCode() {
				return null;
			}
		};
	}

	@Override
	public ParamsQuery getPayer() {
		return new PayerJur(payerInn, payerKpp, payerName);
	}

	public OrderTaxVal getOrderTaxVal() {
		return orderTaxVal;
	}

	@Override
	public String getAccounts() {
		return accounts;
	}

	@Override
	public java.sql.Date getMainDate() {

		return XmlLoader.parseDate(mainDate);
	}

	@Override
	public String getMainNumber() {
		return mainNumber;
	}

	@Override
	public Number getMainSum() {
		return mainSumKopeika;
	}

	@Override
	public java.sql.Date getAddDate() {
		return null; // addDate �� ���� XmlLoader.parseDate(addDate);
	}

	@Override
	public String getAddNumber() {
		return addNumber;
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
	public String getMainStatus() {
		return mainStatus;
	}

	@Override
	public String getMainType() {
		return mainType;
	}

	@Override
	public String getMainDescription() {
		return mainDescription;
	}

	@Override
	public String getSubNumber() {
		return null;
	}

	@Override
	public Date getSubDate() {
		return null;
	}

	@Override
	public String getMainCode() {
		return null;
	}

}
