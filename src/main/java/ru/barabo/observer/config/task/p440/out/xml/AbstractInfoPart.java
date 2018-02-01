package ru.barabo.observer.config.task.p440.out.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.RequestResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.*;

import java.util.Date;


abstract public class AbstractInfoPart {

	@XStreamAlias("���������")
	private String pbFileName;

	@XStreamAlias("��������")
	private String dateCreate = XmlLoader.formatDate(new Date());

	@XStreamAlias("��������")
	private String dateSend = XmlLoader.formatDate(new Date());

	@XStreamAlias("����")
	private FnsXml fns;

	@XStreamAlias("������")
	private BankXml ourBank = BankXml.ourBank();

	@XStreamAlias("����")
	private PayerJur payerJur;

	@XStreamAlias("����")
	private PayerIp payerIp;

	@XStreamAlias("����")
	private PayerPhysic payerPhysic;

	abstract protected void setDateRequest(String dateRequest);

	abstract protected void setNumberRequest(String numberRequest);

	public AbstractInfoPart(RequestResponseData responseData) {

		this.pbFileName = responseData.getPbFileName();

		this.fns = responseData.getFns();

		this.payerJur = responseData.getPayer() == null ? null : responseData.getPayer().getPayerJur();

		this.payerIp = responseData.getPayer() == null ? null : responseData.getPayer().getPayerIp();

		this.payerPhysic = responseData.getPayer() == null ? null : responseData.getPayer().getPayerPhysic();

		setDateRequest(XmlLoader.formatDate(responseData.getDateRequest()));

		setNumberRequest(responseData.getNumberRequest());
	}
}
