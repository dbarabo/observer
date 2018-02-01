package ru.barabo.observer.config.task.p440.out.xml.bnp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.load.xml.impl.BankXml;

import java.util.Date;

@XStreamAlias("����������")
public class BnpInfoPart {

	@XStreamAlias("��������")
	private String numberMessage = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("��������")
	private String numberPno;

	@XStreamAlias("���������")
	private String datePno;

	@XStreamAlias("������")
	private String codeFns;

	@XStreamAlias("�������")
	private String sumPnoKopeika;

	@XStreamAlias("�����")
	private String innPayer;

	@XStreamAlias("�����")
	private String kppPayer;

	@XStreamAlias("��������")
	private String namePayer;

	@XStreamAlias("�������")
	private String sumPartExecKopeika;

	@XStreamAlias("������������")
	private Integer codeWhyNotExecute = 1;

	@XStreamAlias("�������")
	private String descriptionWhyNotExecute;

	@XStreamAlias("��������")
	private String dateSend = XmlLoader.formatDate(new Date());

	@XStreamAlias("�����")
	private String account;

	@XStreamAlias("������")
	private BankXml ourBank = BankXml.ourBank();

	@XStreamAlias("���������")
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
