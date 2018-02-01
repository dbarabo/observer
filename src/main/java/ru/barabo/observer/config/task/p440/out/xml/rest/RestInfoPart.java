package ru.barabo.observer.config.task.p440.out.xml.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart;

import java.util.Date;
import java.util.List;

@XStreamAlias("����������")
public class RestInfoPart extends AbstractInfoPart {

	@XStreamAlias("��������")
	private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("��������")
	private String typeHelp = "1";

	@XStreamAlias("�����")
	private String requestNumber;

	@XStreamAlias("������")
	private String requestDate;

	@XStreamAlias("������")
	private String viewHelp; // ����� �� ����

	@XStreamAlias("�����������")
	private String actualDate = XmlLoader.formatDate(new Date());

	@XStreamImplicit(itemFieldName = "�������")
	private List<RestAccount> restList;

	@XStreamAlias("���������")
	private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

	public RestInfoPart(RestResponseData restResponseData) {
		super(restResponseData);

		this.viewHelp = restResponseData.getViewHelp();

		this.restList = restResponseData.getRestAccountList();
	}

	@Override
	protected void setDateRequest(String dateRequest) {
		this.requestDate = dateRequest;
	}

	@Override
	protected void setNumberRequest(String numberRequest) {
		this.requestNumber = numberRequest;
	}
}
