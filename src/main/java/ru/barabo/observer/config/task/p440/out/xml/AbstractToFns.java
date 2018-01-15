package ru.barabo.observer.config.task.p440.out.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;

import java.util.UUID;

public class AbstractToFns {

	final static transient protected Logger logger = Logger.getLogger(AbstractToFns.class.getName());

	@XStreamAlias("ИдЭС")
	final protected String guid = UUID.randomUUID().toString();

	@XStreamAlias("ВерсПрог")
	final protected String versionProgram = "JAFINA 5.1";

	@XStreamAlias("ТелОтпр")
	final protected String pnoneSender = "(423)222-98-82";

	@XStreamAlias("ФамОтпр")
	final protected String familySender = "БРЫКИНА";

	@XStreamAlias("ДолжнОтпр")
	final protected String fnsPostSender = "Специалист по сопровождению";

	@XStreamAlias("ВерсФорм")
	final protected String fnsFormatVersion = "3.00";

	@XStreamAlias("ТипИнф")
	private String typeInfo;

	public AbstractToFns(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	public String getTypeInfo() {
		return typeInfo;
	}


}
