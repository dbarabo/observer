package ru.barabo.observer.config.cbr.ptkpsd.task.p550.save;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ES_REC")
public class RecordEs {

	//xstream.useAttributeFor(RecordEs.class, "writer");
	//xstream.aliasField("author", Blog.class, "writer");
	
	@XStreamAlias("IdInfoOR")
	private String idInfo;
	
	@XStreamAlias("REZ_ES")
	private Integer resEz = 0;

	public RecordEs(String idInfo) {
		this.idInfo = idInfo;
	}
}
