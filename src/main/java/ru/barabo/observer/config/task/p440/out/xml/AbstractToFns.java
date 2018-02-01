package ru.barabo.observer.config.task.p440.out.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.barabo.p440.out.ResponseData;

import java.util.UUID;

public class AbstractToFns {

	final static transient protected Logger logger = Logger.getLogger(AbstractToFns.class.getName());

	@XStreamAlias("����")
	final protected String guid = UUID.randomUUID().toString();

	@XStreamAlias("��������")
	final protected String versionProgram = "JAFINA 5.1";

	@XStreamAlias("�������")
	final protected String pnoneSender = "(423)222-98-82";

	@XStreamAlias("�������")
	final protected String familySender = "�������";

	@XStreamAlias("���������")
	final protected String fnsPostSender = "���������� �� �������������";

	@XStreamAlias("��������")
	final protected String fnsFormatVersion = "3.00";

	@XStreamAlias("������")
	private String typeInfo;

	public AbstractToFns(ResponseData responseData) {

		this.typeInfo = responseData.typeInfo();
	}

	public String getTypeInfo() {
		return typeInfo;
	}


}
