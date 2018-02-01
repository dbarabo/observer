package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart;

import java.util.List;

@XStreamAlias("����������")
public class ExtractMainInfoPart extends AbstractInfoPart {

	@XStreamAlias("��������")
	private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("��������")
	private String typeHelp = "1";

	@XStreamAlias("�������")
	private String requestNumber;

	@XStreamAlias("��������")
	private String requestDate;

	@XStreamAlias("��������")
	private String viewHelp; // ����� �� ���� <3 | 5 | 8>

	@XStreamAlias("�������")
	private Integer countAddFiles = 0; // ��-���������� ������� ��� ������ ���

	@XStreamImplicit(itemFieldName = "��������")
	private List<ExtractMainAccount> extractMainAccountList;

	@XStreamAlias("���������")
	private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

	public ExtractMainInfoPart(ExtractMainResponseData extractMainResponseData) {
		super(extractMainResponseData);

		this.viewHelp = extractMainResponseData.getViewHelp();

		this.extractMainAccountList = extractMainResponseData.getExtractMainAccountList();

		this.countAddFiles = extractMainResponseData.getCountAddFiles();
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
