package ru.barabo.observer.config.task.p440.out.xml.ver4.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.RestResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileRestXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-440P:spr4:v4.00..3";

    @XStreamAlias("СПРОБОСТАТ")
    private RestInfoPartVer4 restInfoPartVer4;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileRestXmlVer4(RestResponseData restResponseData) {

        super(restResponseData);

        this.restInfoPartVer4 = new RestInfoPartVer4(restResponseData);
    }
}
