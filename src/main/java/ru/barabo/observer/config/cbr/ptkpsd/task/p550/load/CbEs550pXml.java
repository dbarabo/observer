package ru.barabo.observer.config.cbr.ptkpsd.task.p550.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СообщОтказ_115ФЗ")
public class CbEs550pXml {

	@XStreamAlias("СлужЧасть")
	protected ServicePart servicePart;

	@XStreamAlias("ИнформЧасть")
	protected InfoPart550 infoPart;

	public InfoPart550 getInfoPart() {
		return infoPart;
	}

	public ServicePart getServicePart() {
		return servicePart;
	}
}
