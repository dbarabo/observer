package ru.barabo.observer.config.cbr.ptkpsd.task.p550.save;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.CbEs550pXml;
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.InfoPart550;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@XStreamAlias("KVIT")
public class Kvit550 {

	@XStreamAlias("IDNOR")
	private String bank = "0021_0000";

	@XStreamAlias("ES")
	private String sourceFileName;

	@XStreamAlias("SIZE_ES")
	private Integer sourceFileSize;

	@XStreamAlias("DATE_ES")
	private String dateEsMessage;

	@XStreamAlias("RECNO_ES")
	private RecordEsList recordEsList;

	@XStreamAlias("DATE_KVIT")
	private String dateKvit = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

	@XStreamAlias("TIME_KVIT")
	private String timeKvit = new SimpleDateFormat("HH:mm:ss").format(new Date());

	@XStreamAlias("OPER")
	private String oper = "БРЫКИНА";

	@XStreamAlias("TEL_OPER")
	private String phone = "(423)222-98-82";

	public Kvit550(CbEs550pXml xmlData, File sourceFile) {

		this.sourceFileName = sourceFile.getName();

		this.sourceFileSize = new Long(sourceFile.length()).intValue();

		this.dateEsMessage = xmlData.getServicePart().getDateMessage();

		fillRecords(xmlData.getInfoPart());
	}

	private void fillRecords(InfoPart550 infoPart) {

		recordEsList = new RecordEsList(infoPart);
	}
}
