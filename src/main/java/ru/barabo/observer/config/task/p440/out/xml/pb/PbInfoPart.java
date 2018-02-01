package ru.barabo.observer.config.task.p440.out.xml.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@XStreamAlias("����������")
public class PbInfoPart {

	@XStreamAlias("��������")
	private String fileName;

	@XStreamAlias("�����������������")
	final private String checkEnd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());


	@XStreamImplicit(itemFieldName = "���������")
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
