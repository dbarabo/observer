package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart;

import java.util.List;

@XStreamAlias("ВЫПБНОСНОВ")
public class ExtractMainInfoPart extends AbstractInfoPart {

	@XStreamAlias("НомВыпис")
	private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("ТипВыпис")
	private String typeHelp = "1";

	@XStreamAlias("НомЗапр")
	private String requestNumber;

	@XStreamAlias("ДатаЗапр")
	private String requestDate;

	@XStreamAlias("ВидВыпис")
	private String viewHelp; // берем из базы <3 | 5 | 8>

	@XStreamAlias("КолДопФ")
	private Integer countAddFiles = 0; // по-умолачинию считаем что файлов нет

	@XStreamImplicit(itemFieldName = "Сведения")
	private List<ExtractMainAccount> extractMainAccountList;

	@XStreamAlias("ПредБанка")
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
