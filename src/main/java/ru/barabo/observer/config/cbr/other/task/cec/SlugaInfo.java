package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("СлужИнфо")
public class SlugaInfo {
	
	@XStreamAlias("ВРНКанд")
	@XStreamAsAttribute
	private String Vr; // ВРНКанд

	@XStreamAlias("ИДИнфо")
	private IdInfoSluga idInfo;
	
	@XStreamAlias("Наименование")
	private NameInfoSluga NameInfoSluga;

	public String getVr() {
		return Vr;
	}

	public IdInfoSluga getIdInfo() {
		return idInfo;
	}

	public NameInfoSluga getNameInfoSluga() {
		return NameInfoSluga;
	}
}
