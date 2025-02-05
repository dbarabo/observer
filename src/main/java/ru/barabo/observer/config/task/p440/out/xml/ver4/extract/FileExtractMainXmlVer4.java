package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileExtractMainXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-operac:oper4:v4.00..3";

    @XStreamAlias("ВЫПБНОСНОВ")
    private ExtractMainInfoPartVer4 extractMainInfoPartVer4;

    @XStreamAlias("ПредБанка")
    private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileExtractMainXmlVer4(ExtractResponseData extractMainResponseData) {

        super(extractMainResponseData);

        extractMainInfoPartVer4 = new ExtractMainInfoPartVer4(extractMainResponseData);
    }
}
