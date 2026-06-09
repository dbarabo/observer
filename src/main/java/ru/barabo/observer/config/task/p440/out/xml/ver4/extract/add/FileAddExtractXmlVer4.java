package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.AddExtractResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileAddExtractXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns;

    @XStreamAlias("ВЫПБНДОПОЛ")
    private AddExtractInfoPartVer4 addExtractInfoPartVer4;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileAddExtractXmlVer4(AddExtractResponseDataVer4 addExtractResponseData) {

        super(addExtractResponseData);

        this.xmlns = isOldFormat4(addExtractResponseData.versionRequest() )
                ?  "urn:cbr-440P:oper4:v4.00..3" : "urn:cbr-6952U:oper4:v4.02..3";

        this.addExtractInfoPartVer4 = new AddExtractInfoPartVer4(addExtractResponseData);
    }
}
