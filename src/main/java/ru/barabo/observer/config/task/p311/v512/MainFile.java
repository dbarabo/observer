package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Файл")
public final class MainFile {

    @XStreamAlias("ИдФайл")
    private final String idFile;

    @XStreamAlias("ТипИнф")
    private final String typeInfo = "СООБЩБАНКА";

    @XStreamAlias("ВерсПрог")
    private final String versionProgram = "JAFINA 5.8";

    @XStreamAlias("ТелОтпр")
    private final String phoneSender = "(423)222-87-78";

    @XStreamAlias("ДолжнОтпр")
    private final String positionSender = "Специалист по сопровождению";

    @XStreamAlias("ФамОтпр")
    private final String secondNameSender = "КОЛТОВСКАЯ";

    @XStreamAlias("КолДок")
    private final String coundDocs = "1";

    @XStreamAlias("ВерсФорм")
    private final String versionFormat = "5.12";

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
