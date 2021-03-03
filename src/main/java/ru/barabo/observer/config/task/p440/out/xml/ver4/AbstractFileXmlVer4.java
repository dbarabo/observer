package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.ResponseData;

import java.util.UUID;

public class AbstractFileXmlVer4 {
    @XStreamAlias("ИдЭС")
    final protected String guid = UUID.randomUUID().toString();

    @XStreamAlias("ТипИнф")
    private String typeInfo;

    @XStreamAlias("ВерсПрог")
    final protected String versionProgram = "JAFINA 5.1";

    @XStreamAlias("ТелОтпр")
    final protected String pnoneSender = "(423)222-98-82";

    @XStreamAlias("ДолжнОтпр")
    final protected String fnsPostSender = "Специалист по сопровождению";

    @XStreamAlias("ФамОтпр")
    final protected String familySender = "Колтовская А В";

    @XStreamAlias("ВерсФорм")
    final protected String fnsFormatVersion = "4.00";

    public AbstractFileXmlVer4(ResponseData responseData) {
        this.typeInfo = responseData.typeInfo();
    }
}
