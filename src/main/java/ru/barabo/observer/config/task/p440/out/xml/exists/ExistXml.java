package ru.barabo.observer.config.task.p440.out.xml.exists;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("‘‡ÈÎ")
public class ExistXml extends AbstractToFns {

	@XStreamAlias("—œ–¡ÕÕ¿À»◊")
	private ExistInfoPart existInfoPart;

	public ExistXml(ExistsResponseData existResponseData) {
		super(existResponseData);

		this.existInfoPart = new ExistInfoPart(existResponseData);
	}

	public ExistInfoPart getExistInfoPart() {
		return existInfoPart;
	}
}