package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.AddExtractResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileAddExtractXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-440P:oper4:v4.00..3";

    @XStreamAlias("ВЫПБНДОПОЛ")
    private AddExtractInfoPartVer4 addExtractInfoPartVer4;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileAddExtractXmlVer4(AddExtractResponseDataVer4 addExtractResponseData) {

        super(addExtractResponseData);

        this.addExtractInfoPartVer4 = new AddExtractInfoPartVer4(addExtractResponseData);
    }
}
