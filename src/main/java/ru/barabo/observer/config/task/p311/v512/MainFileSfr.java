package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("sb:Файл")
public class MainFileSfr {

    @XStreamAsAttribute
    @XStreamAlias("xmlns:sb")
    final String xmlnsSb = "urn://iis.ecp.ru/bank-account-information/FRS0_101";

    @XStreamAsAttribute
    @XStreamAlias("xmlns:xsi")
    final String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

    @XStreamAlias("ИдФайл")
    @XStreamAsAttribute
    private final String idFile;

    @XStreamAlias("ТипИнф")
    @XStreamAsAttribute
    private final String typeInfo = "СООБЩБАНКА";

    @XStreamAlias("ВерсПрог")
    @XStreamAsAttribute
    private final String versionProgram = "JAFINA 5.8";

    @XStreamAlias("ТелОтпр")
    @XStreamAsAttribute
    private final String phoneSender = "(423)222-87-78";

    @XStreamAlias("ДолжнОтпр")
    @XStreamAsAttribute
    private final String positionSender = "Специалист по сопровождению";

    @XStreamAlias("ФамОтпр")
    @XStreamAsAttribute
    private final String secondNameSender = "КОЛТОВСКАЯ";

    @XStreamAlias("КолДок")
    @XStreamAsAttribute
    private final String coundDocs = "1";

    @XStreamAlias("ВерсФорм")
    @XStreamAsAttribute
    private final String versionFormat = "1.0.1";

    @XStreamAlias("Документ")
    private final MainDocument mainDocument;

    public MainFileSfr(String idFile,  MainDocument mainDocument) {
        this.idFile = idFile;
        this.mainDocument = mainDocument;
    }

    public boolean isFakeFile() {
        return mainDocument == null;
    }
}
