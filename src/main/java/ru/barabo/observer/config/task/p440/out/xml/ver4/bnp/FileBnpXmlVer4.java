package ru.barabo.observer.config.task.p440.out.xml.ver4.bnp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.barabo.p440.out.data.BnpResponseDataVer4;
import ru.barabo.observer.config.task.p440.load.xml.SenderBank;
import ru.barabo.observer.config.task.p440.out.xml.ver4.AbstractFileXmlVer4;

@XStreamAlias("Файл")
public class FileBnpXmlVer4 extends AbstractFileXmlVer4 {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns;

    @XStreamAlias("СБЩБННЕИСП")
    private BnpInfoPartVer4 bnpInfoPart;

    @XStreamAlias("ПредБанка")
    final private SenderBank senderBank = SenderBank.OUR_MAIN_UOD;

    public FileBnpXmlVer4(BnpResponseDataVer4 bnpResponseData) {
        super(bnpResponseData);

        this.xmlns = "3.72".equals(bnpResponseData.versionRequest() ) ? "urn:cbr-440P:soob4:v4.00..3" : "urn:cbr-6952U:soob4:v4.02..3";

        this.bnpInfoPart = new BnpInfoPartVer4(bnpResponseData);
    }

    public BnpInfoPartVer4 getBnpInfoPart() {

        return bnpInfoPart;
    }
}
