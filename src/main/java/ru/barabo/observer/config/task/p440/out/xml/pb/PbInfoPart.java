package ru.barabo.observer.config.task.p440.out.xml.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@XStreamAlias("ПОДБНПРИНТ")
public class PbInfoPart {

	@XStreamAlias("ИмяФайла")
	private String fileName;

	@XStreamAlias("ДатаВремяПроверки")
	final private String checkEnd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());


	@XStreamImplicit(itemFieldName = "Результат")
	private List<PbResult> pbResultList;

	public PbInfoPart(String fileName, List<PbResult> pbResultList) {

		this.fileName = fileName;
		this.pbResultList = pbResultList;
	}

	public String getFileName() {
		return fileName;
	}

	public List<PbResult> getPbResultList() {
		return pbResultList;
	}
}
