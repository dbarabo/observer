package ru.barabo.observer.config.cbr.ptkpsd.task.p550.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ИнформЧасть")
public class InfoPart550 {

	@XStreamAlias("ТипОрганизация")
	private String typeOrg;

	@XStreamImplicit(itemFieldName = "Раздел1.1")
	private List<Part11> part11;

	@XStreamImplicit(itemFieldName = "Раздел1.2")
	private List<Part12> part12;

	@XStreamImplicit(itemFieldName = "Раздел2")
	private List<Part2> part2;

	public List<Part11> getPart11() {
		return part11;
	}

	public List<Part12> getPart12() {
		return part12;
	}

	public List<Part2> getPart2() {
		return part2;
	}

}
