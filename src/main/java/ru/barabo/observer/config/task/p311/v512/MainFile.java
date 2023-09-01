package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Файл")
public final class MainFile {

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
    private final String versionFormat = "5.13";

    @XStreamAlias("Документ")
    private final MainDocument mainDocument;

    public MainFile(String idFile,  MainDocument mainDocument) {
        this.idFile = idFile;
        this.mainDocument = mainDocument;
    }

    public boolean isFakeFile() {
        return mainDocument == null;
    }
}
