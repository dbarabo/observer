package ru.barabo.observer.config.task.p440.load.xml.pno;

import ru.barabo.observer.config.task.p440.load.xml.Bank;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PnoDataImpl implements PnoData {

	private Bank benBank;

	private PayerJur benecifiar;

	private String benAccount;

	private String queuePay;
	private String codePay;
	private String vidPorPay;
	private String unkgn;
	private String kbkPay;
	private String oktmoPay;
	private String codeOsnPay;
	private String srokTrebPay;
	private String dateTrebPay;
	private String numberTrebPay;
	private OrderTaxVal orderTaxVal;

	public PnoDataImpl(String benBankBik, String benBankName, String benAccount,
			String benInn, String benKpp, String benName, String queuePay, String codePay,
			String vidPorPay, String unkgn, String kbkPay, String oktmoPay, String codeOsnPay,
			String srokTrebPay, String dateTrebPay, String numberTrebPay, OrderTaxVal orderTaxVal) {

		benBank = new BankXml(benBankBik, benBankName);

		benecifiar = new PayerJur(benInn, benKpp, benName);

		this.benAccount = benAccount;

		this.queuePay = queuePay;
		this.codePay = codePay;
		this.vidPorPay = vidPorPay;
		this.unkgn = unkgn;
		this.kbkPay = kbkPay;

		this.oktmoPay = oktmoPay;
		this.codeOsnPay = codeOsnPay;
		this.srokTrebPay = srokTrebPay;
		this.dateTrebPay = dateTrebPay;
		this.numberTrebPay = numberTrebPay;

		this.orderTaxVal = orderTaxVal;
	}

	@Override
	public Bank getBenBank() {
		return benBank;
	}

	@Override
	public String getBenAccount() {
		return benAccount;
	}

	@Override
	public PayerJur getBenName() {
		return benecifiar;
	}

	@Override
	public String getQueuePay() {
		return queuePay;
	}

	@Override
	public String getCodePay() {
		return codePay;
	}

	@Override
	public String getVidPor() {
		return vidPorPay;
	}

	@Override
	public String getUnkgnPay() {
		return unkgn;
	}

	@Override
	public String getKbkPay() {
		return kbkPay;
	}

	@Override
	public String getOktmoPay() {
		return oktmoPay;
	}

	@Override
	public String getCodeOsnPay() {
		return codeOsnPay;
	}

	@Override
	public String getSrokTrebPay() {
		return srokTrebPay;
	}

	@Override
	public String getDateTrebPay() {
		return dateTrebPay;
	}

	@Override
	public String getNumberTrebPay() {
		return numberTrebPay;
	}

	@Override
	public List<Object> getParams() {

		return new ArrayList<Object>(Arrays.asList(
				benBank.getName() == null ? String.class : benBank.getName(),
				benBank.getBik() == null ? String.class : benBank.getBik(),
				benecifiar.getInn() == null ? String.class : benecifiar.getInn(),
				benecifiar.getKpp() == null ? String.class : benecifiar.getKpp(),
				benecifiar.getName() == null ? String.class : benecifiar.getName(),
				benAccount == null ? String.class : benAccount,
				queuePay == null ? String.class : queuePay,
				codePay == null ? String.class : codePay,
				vidPorPay == null ? String.class : vidPorPay,
				unkgn == null ? String.class : unkgn,
				kbkPay == null ? String.class : kbkPay,
				oktmoPay == null ? String.class : oktmoPay,
				codeOsnPay == null ? String.class : codeOsnPay,
				srokTrebPay == null ? String.class : srokTrebPay,
				dateTrebPay == null ? String.class : dateTrebPay,
				numberTrebPay == null ? String.class : numberTrebPay,

				orderTaxVal == null || orderTaxVal.getNumberPorVal() == null
						? String.class : orderTaxVal.getNumberPorVal(),

				orderTaxVal == null || orderTaxVal.getDatePorVal() == null
						? String.class : orderTaxVal.getDatePorVal(),

				orderTaxVal == null || orderTaxVal.getAccountPorVal() == null
						? String.class : orderTaxVal.getAccountPorVal()));
	}

	private static String COLUMNS = "BEN_BANK_NAME, BEN_BANK_BIK, BEN_INN, BEN_KPP, BEN_NAME, BEN_ACCOUNT, "
								   + "QUEUE_PAY, CODE_PAY, VIDPOR_PAY, UNKGN_PAY, KBK_PAY, OKTMO_PAY, CODEOSN_PAY, "
								   + "SROK_TREB_PAY, DATE_TREB_PAY, NUMBER_TREB_PAY, "
								   + "CURRENCY_NUMBER_TRANSFER, CURRENCY_DATE_TRANSFER, CURRENCY_ACCOUNT, "
								   + "FNS_FROM, ID";

	@Override
	public String getListColumns() {

		return COLUMNS;
	}
}
