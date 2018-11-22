package ru.barabo.observer.config.task.p440.out.xml.extract;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.AdditionalResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("Файл")
public class AdditionalExtractXml extends AbstractToFns {

	@XStreamAlias("ВЫПБНДОПОЛ")
	private AddExtractInfoPart addExtractInfoPart;

	public AdditionalExtractXml(AdditionalResponseData additionalResponseData) {
		super(additionalResponseData);

		addExtractInfoPart = new AddExtractInfoPart(additionalResponseData);
	}

	public AddExtractInfoPart getAddExtractInfoPart() {
		return addExtractInfoPart;
	}
}
