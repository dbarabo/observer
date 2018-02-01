package ru.barabo.observer.config.task.p440.out.xml.bnp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

import java.util.Date;

@XStreamAlias("СБЩБННЕИСП")
public class BnpInfoPart {

	@XStreamAlias("НомСообщ")
	private String numberMessage = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("НомПоруч")
	private String numberPno;

	@XStreamAlias("ДатаПоруч")
	private String datePno;

	@XStreamAlias("КодНОБ")
	private String codeFns;

	@XStreamAlias("СумПлат")
	private String sumPnoKopeika;

	@XStreamAlias("ИНННП")
	private String innPayer;

	@XStreamAlias("КППНП")
	private String kppPayer;

	@XStreamAlias("Плательщ")
	private String namePayer;

	@XStreamAlias("СумЧаст")
	private String sumPartExecKopeika;

	@XStreamAlias("КодПричНеисп")
	private Integer codeWhyNotExecute = 1;

	@XStreamAlias("ДопСвед")
	private String descriptionWhyNotExecute;

	@XStreamAlias("ДатаНапр")
	private String dateSend = XmlLoader.formatDate(new Date());

	@XStreamAlias("НомСч")
	private String account;

	@XStreamAlias("СвБанк")
	private BankXml ourBank = BankXml.ourBank();

	@XStreamAlias("ПредБанка")
	private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

	public BnpInfoPart(BnpResponseData responseData) {

		this.numberPno = responseData.getNumberPno();

		this.datePno = XmlLoader.formatDate(responseData.getDatePno());

		this.codeFns = responseData.getCodeFns();

		this.sumPnoKopeika = XmlLoader.formatInteger(responseData.getSumPnoKopeika());

		this.innPayer = responseData.getPayer().getInn();

		this.kppPayer = responseData.getPayer().getKpp();

		this.namePayer = responseData.getPayer().getName();

		this.sumPartExecKopeika = XmlLoader.formatInteger(responseData.getSumPartExecKopeika());

		this.account = responseData.getAccount();
	}
}
