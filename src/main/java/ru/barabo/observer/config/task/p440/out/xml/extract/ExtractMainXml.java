package ru.barabo.observer.config.task.p440.out.xml.extract;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

/**
 * главный файл выписки
 * 
 * @author debara
 *
 */
@XStreamAlias("‘айл")
public class ExtractMainXml extends AbstractToFns {

	@XStreamAlias("¬џѕЅЌќ—Ќќ¬")
	private ExtractMainInfoPart extractMainInfoPart;

	public ExtractMainXml(ExtractMainResponseData extractMainResponseData) {
		super(extractMainResponseData);

		this.extractMainInfoPart = new ExtractMainInfoPart(extractMainResponseData);
	}

	public ExtractMainInfoPart getExtractMainInfoPart() {
		return extractMainInfoPart;
	}

}
