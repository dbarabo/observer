package ru.barabo.observer.config.task.p440.load.xml.pno;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.Fns;
import ru.barabo.observer.config.task.p440.load.xml.FromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.sql.Date;

@XStreamAlias("ПОРУЧСЧЕТН")
public final class OrderTax implements FromFnsInfo {

	@XStreamAlias("БИКБПл")
	private String bankBik;

	@XStreamAlias("БанкПл")
	private String bankName;

	@XStreamAlias("ДатаПоруч")
	private String mainDate;

	@XStreamAlias("ДатаТреб")
	private String addDate;

	@XStreamAlias("ИНННП")
	private String payerInn;

	@XStreamAlias("КБК")
	private String kbkPay;

	@XStreamAlias("КППНП")
	private String payerKpp;

	@XStreamAlias("НазнПл")
	private String mainDescription;

	@XStreamAlias("НомПоруч")
	private String mainNumber;

	@XStreamAlias("НомСчПл")
	private String accounts;

	@XStreamAlias("НомТреб")
	private String addNumber;

	@XStreamAlias("ОКТМО")
	private String oktmoPay;

	@XStreamAlias("ОчерПл")
	private String queuePay;

	@XStreamAlias("Плательщ")
	private String payerName;

	@XStreamAlias("СумПлат")
	private Integer mainSumKopeika;

	@XStreamAlias("Статус")
	private String mainStatus;

	@XStreamAlias("ВидОп")
	private String mainType;

	@XStreamAlias("ВидПор")
	private String vidPorPay;

	@XStreamAlias("КодПл")
	private String codePay;

	@XStreamAlias("КодОсн")
	private String codeOsnPay;

	@XStreamAlias("СрокУплТр")
	private String srokTrebPay;

	@XStreamAlias("БИКБПол")
	private String benBankBik;

	@XStreamAlias("БанкПол")
	private String benBankName;

	@XStreamAlias("ИННПол")
	private String benInn;

	@XStreamAlias("КПППол")
	private String benKpp;

	@XStreamAlias("Получ")
	private String benName;

	@XStreamAlias("НомСчПол")
	private String benAccount;

	@XStreamAlias("УНКГН")
	private String unkgn;

	@XStreamAlias("ПорВал")
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
		return null;
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
