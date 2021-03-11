package ru.barabo.observer.config.task.p440.out.xml.ver4.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseDataVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FilePbXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-440P:pod4:v4.00..3";

    @XStreamAlias("ПОДБНПРИНТ")
    private PbInfoPartVer4 bbInfoPart;

    public FilePbXmlVer4(PbResponseDataVer4 pbResponseData) {

        super(pbResponseData);

        this.bbInfoPart = new PbInfoPartVer4(pbResponseData.fileNameFromFns(),
                pbResponseData.getPbResultList());
    }

    public PbInfoPartVer4 getPbInfoPart() {
        return bbInfoPart;
    }
}
