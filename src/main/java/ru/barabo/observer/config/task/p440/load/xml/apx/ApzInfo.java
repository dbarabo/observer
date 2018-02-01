package ru.barabo.observer.config.task.p440.load.xml.apx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("РЕШНОТЗПОР")
public class ApzInfo extends AbstractApx {

	@XStreamImplicit(itemFieldName = "ОтозвПоруч")
	private List<PnoCancel> pnoCancel;

	@Override
	protected List<PnoCancel> getPnoCancelList() {
		return pnoCancel;
	}
}

