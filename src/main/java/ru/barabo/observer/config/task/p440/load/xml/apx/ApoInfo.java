package ru.barabo.observer.config.task.p440.load.xml.apx;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class ApoInfo extends AbstractApx {

	@XStreamAlias("НомОтмРеш")
	private String subNumber;

	@XStreamAlias("ДатаОтмРеш")
	private String subDate;

	@XStreamImplicit(itemFieldName = "ВозобнПоруч")
	private List<PnoCancel> pnoCancel;

	@Override
	protected List<PnoCancel> getPnoCancelList() {
		return pnoCancel;
	}
}
