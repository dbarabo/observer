package ru.barabo.observer.config.task.p440.out.xml.bnp;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseData;
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns;

@XStreamAlias("Файл")
public class BnpXml extends AbstractToFns {

	@XStreamAlias("СБЩБННЕИСП")
	private BnpInfoPart bnpInfoPart;

	public BnpXml(BnpResponseData bnpResponseData) {
		super(bnpResponseData);

		this.bnpInfoPart = new BnpInfoPart(bnpResponseData);
	}

	public BnpInfoPart getBnpInfoPart() {
		return bnpInfoPart;
	}
}
