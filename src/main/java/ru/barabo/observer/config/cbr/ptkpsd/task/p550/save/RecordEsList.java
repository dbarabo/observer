package ru.barabo.observer.config.cbr.ptkpsd.task.p550.save;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.load.InfoPart550;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("RECNO_ES")
public class RecordEsList {

	@XStreamAlias("nRec")
	private Long countRecord;

	@XStreamImplicit(itemFieldName = "ES_REC")
	private List<RecordEs> recordEs;

	public RecordEsList(InfoPart550 infoPart) {

		this.recordEs = new ArrayList<RecordEs>();
		
		if(infoPart.getPart11() != null) {
			infoPart.getPart11().stream().forEach(part ->
					recordEs.add(new RecordEs(part.getNumberRecord())));
		}

		if (infoPart.getPart12() != null) {
			infoPart.getPart12().stream().forEach(part ->
					recordEs.add(new RecordEs(part.getNumberRecord())));
		}

		if (infoPart.getPart2() != null) {
			infoPart.getPart2().stream().forEach(part ->
					recordEs.add(new RecordEs(part.getNumberRecord())));
		}

		countRecord = new Integer(recordEs.size()).longValue();
	}
}
