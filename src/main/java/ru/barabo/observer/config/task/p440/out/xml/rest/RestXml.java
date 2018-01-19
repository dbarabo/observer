package ru.barabo.observer.config.task.p440.out.xml.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("Файл")
public class RestXml extends AbstractToFns {

	@XStreamAlias("СПРБНОСТАТ")
	private RestInfoPart restInfoPart;

	public RestXml(RestResponseData restResponseData) {
		super(restResponseData);

		this.restInfoPart = new RestInfoPart(restResponseData);
	}

	public RestInfoPart getRestInfoPart() {
		return restInfoPart;
	}
}