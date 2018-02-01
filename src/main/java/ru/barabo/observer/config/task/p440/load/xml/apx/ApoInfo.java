package ru.barabo.observer.config.task.p440.load.xml.apx;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class ApoInfo extends AbstractApx {

	@XStreamAlias("���������")
	private String subNumber;

	@XStreamAlias("����������")
	private String subDate;

	@XStreamImplicit(itemFieldName = "�����������")
	private List<PnoCancel> pnoCancel;

	@Override
	protected List<PnoCancel> getPnoCancelList() {
		return pnoCancel;
	}
}
