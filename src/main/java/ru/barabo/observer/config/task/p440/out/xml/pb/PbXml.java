package ru.barabo.observer.config.task.p440.out.xml.pb;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("Файл")
public class PbXml extends AbstractToFns {

	@XStreamAlias("ПОДБНПРИНТ")
	private PbInfoPart bbInfoPart;

	public PbXml(PbResponseData pbResponseData) {
		super("ПОДБНПРИНТ");

		this.bbInfoPart = new PbInfoPart(pbResponseData.fileNameFromFns(),
				pbResponseData.getPbResultList());
	}

	public PbInfoPart getBbInfoPart() {
		return bbInfoPart;
	}
}
