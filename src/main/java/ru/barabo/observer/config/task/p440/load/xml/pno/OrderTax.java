package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.Fns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.sql.Date;

@XStreamAlias("ÏÎĞÓ×Ñ×ÅÒÍ")
public final class OrderTax implements FromFnsInfo {

	@XStreamAlias("ÁÈÊÁÏë")
	private String bankBik;

	@XStreamAlias("ÁàíêÏë")
	private String bankName;

	@XStreamAlias("ÄàòàÏîğó÷")
	private String mainDate;

	@XStreamAlias("ÄàòàÒğåá")
	private String addDate;

	@XStreamAlias("ÈÍÍÍÏ")
	private String payerInn;

	@XStreamAlias("ÊÁÊ")
	private String kbkPay;

	@XStreamAlias("ÊÏÏÍÏ")
	private String payerKpp;

	@XStreamAlias("ÍàçíÏë")
	private String mainDescription;

	@XStreamAlias("ÍîìÏîğó÷")
	private String mainNumber;

	@XStreamAlias("ÍîìÑ÷Ïë")
	private String accounts;

	@XStreamAlias("ÍîìÒğåá")
	private String addNumber;

	@XStreamAlias("ÎÊÒÌÎ")
	private String oktmoPay;

	@XStreamAlias("Î÷åğÏë")
	private String queuePay;

	@XStreamAlias("Ïëàòåëüù")
	private String payerName;

	@XStreamAlias("ÑóìÏëàò")
	private Integer mainSumKopeika;

	@XStreamAlias("Ñòàòóñ")
	private String mainStatus;

	@XStreamAlias("ÂèäÎï")
	private String mainType;

	@XStreamAlias("ÂèäÏîğ")
	private String vidPorPay;

	@XStreamAlias("ÊîäÏë")
	private String codePay;

	@XStreamAlias("ÊîäÎñí")
	private String codeOsnPay;

	@XStreamAlias("ÑğîêÓïëÒğ")
	private String srokTrebPay;

	@XStreamAlias("ÁÈÊÁÏîë")
	private String benBankBik;

	@XStreamAlias("ÁàíêÏîë")
	private String benBankName;

	@XStreamAlias("ÈÍÍÏîë")
	private String benInn;

	@XStreamAlias("ÊÏÏÏîë")
	private String benKpp;

	@XStreamAlias("Ïîëó÷")
	private String benName;

	@XStreamAlias("ÍîìÑ÷Ïîë")
	private String benAccount;

	@XStreamAlias("ÓÍÊÃÍ")
	private String unkgn;

	@XStreamAlias("ÏîğÂàë")
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
		return null; // addDate ÍÅ ÄÀÒÀ XmlLoader.parseDate(addDate);
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
