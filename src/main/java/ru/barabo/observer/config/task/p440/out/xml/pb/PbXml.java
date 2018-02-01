package ru.barabo.observer.config.task.p440.out.xml.pb;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("‘‡ÈÎ")
public class PbXml extends AbstractToFns {

	@XStreamAlias("œŒƒ¡Õœ–»Õ“")
	private PbInfoPart bbInfoPart;

	public PbXml(PbResponseData pbResponseData) {
		super(pbResponseData);

		this.bbInfoPart = new PbInfoPart(pbResponseData.fileNameFromFns(),
				pbResponseData.getPbResultList());
	}

	public PbInfoPart getBbInfoPart() {
		return bbInfoPart;
	}
}
