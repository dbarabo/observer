package ru.barabo.observer.config.task.p440.out.xml.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart;

import java.util.Date;
import java.util.List;

@XStreamAlias("СПРБННАЛИЧ")
public class ExistInfoPart extends AbstractInfoPart {

	@XStreamAlias("НомСправ")
	private String numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

	@XStreamAlias("ТипСправ")
	private String typeHelp = "1";

	@XStreamAlias("НомЗапр")
	private String requestNumber;

	@XStreamAlias("ДатаЗапр")
	private String requestDate;

	@XStreamAlias("ВидСпр")
	private String viewHelp; // берем из базы

	@XStreamAlias("ДейстПоСост")
	private String actualDate = XmlLoader.formatDate(new Date());

	@XStreamImplicit(itemFieldName = "Сведения")
	private List<ExistsAccount> existsList;

	@XStreamAlias("ПредБанка")
	private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

	public ExistInfoPart(ExistsResponseData responseData) {
		super(responseData);

		this.viewHelp = responseData.getViewHelp();

		this.existsList = responseData.getExistsAccountList();
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
