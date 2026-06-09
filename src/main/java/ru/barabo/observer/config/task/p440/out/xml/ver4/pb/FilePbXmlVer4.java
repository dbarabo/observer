package ru.barabo.observer.config.task.p440.out.xml.ver4.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.PbResponseDataVer4;
import ru.barabo.observer.config.fns.cbr.extract.PbResponseDataCbr;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FilePbXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns;

    @XStreamAlias("ПОДБНПРИНТ")
    private PbInfoPartVer4 bbInfoPart;

    public FilePbXmlVer4(PbResponseDataVer4 pbResponseData) {

        super(pbResponseData);

        this.xmlns = isOldFormat4(pbResponseData.versionRequest())
                ? "urn:cbr-440P:pod4:v4.00..3" : "urn:cbr-6952U:pod4:v4.02..3";

        this.bbInfoPart = new PbInfoPartVer4(pbResponseData.fileNameFromFns(),
                pbResponseData.getPbResultList(), pbResponseData.getDateQueue());
    }

    public FilePbXmlVer4(PbResponseDataCbr pbResponseDataCbr) {

        super(pbResponseDataCbr);

        this.xmlns = "urn:cbr-440P:pod4:v4.00..3";

        this.bbInfoPart = new PbInfoPartVer4(pbResponseDataCbr.fileNameFromFns(),
                pbResponseDataCbr.getPbResult(), null );
    }

    public PbInfoPartVer4 getPbInfoPart() {
        return bbInfoPart;
    }
}
