package ru.barabo.observer.config.task.p440.out.xml.ver4.exists;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileExistsXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-440P:spr4:v4.00..3";

    @XStreamAlias("СПРБННАЛИЧ")
    private ExistsInfoPartVer4 existsInfoPartVer4;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileExistsXmlVer4(ExistsResponseData existsResponseData) {

        super(existsResponseData);

        this.existsInfoPartVer4 = new ExistsInfoPartVer4(existsResponseData);
    }
}
