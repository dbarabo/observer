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
    final protected String fnsPostSender = "Зам начальника ДСПО";

    @XStreamAlias("ФамОтпр")
    final protected String familySender = "Барабошкин Д В";

    @XStreamAlias("ВерсФорм")
    protected String fnsFormatVersion;

    public AbstractFileXmlVer4(ResponseData responseData) {

        this.fnsFormatVersion = isOldFormat4(responseData.versionRequest()) ? "4.00" : "4.02";

        this.typeInfo = responseData.typeInfo();
    }

    private boolean isOldFormat4(String formatValue) {

        if(formatValue == null) return true;

        return "4.00".equals(formatValue) ||
               "3.72".equals(formatValue) ||
               "3.01".equals(formatValue);
    }
}
