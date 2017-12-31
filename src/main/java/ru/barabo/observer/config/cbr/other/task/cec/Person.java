package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Персона")
public class Person {
	@XStreamAsAttribute
    @XStreamAlias("Ид")
	private String id;
	
	@XStreamAlias("ПерсИнфо")
	private PersInfo persInfo;
	
	@XStreamAlias("СлужИнфо")
	private SlugaInfo slugaInfo;

	public String getId() {
		return id;
	}

	public PersInfo getPersInfo() {
		return persInfo;
	}

	public SlugaInfo getSlugaInfo() {
		return slugaInfo;
	}
}
