package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.ExtractResponseData;
import ru.barabo.observer.config.task.p440.out.xml.ver4.ClientVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.GeneralInfoPartVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoBankPBRVer4;
import ru.barabo.observer.config.task.p440.out.xml.ver4.InfoRequestDetail;

@XStreamAlias("ВЫПБНОСНОВ")
public class ExtractMainInfoPartVer4 extends GeneralInfoPartVer4 {

    @XStreamAlias("НаимПодтв")
    private String pbFile;

    @XStreamAlias("ВидВыпис")
    private String viewHelp; // берем из базы <3 | 5 | 8>

    @XStreamAlias("СведЗапр")
    private InfoRequestDetail infoRequestDetail;

    @XStreamAlias("СведБанкПБР")
    final private InfoBankPBRVer4 bank = InfoBankPBRVer4.ourBank();

    @XStreamAlias("КлиентСчет")
    private ClientVer4 client;

    @XStreamAlias("СведенияВып")
    private InfoExtractDetail infoExtractDetail;

    public ExtractMainInfoPartVer4(ExtractResponseData extractMainResponseData) {

        super(true);

        this.pbFile = extractMainResponseData.getPbFileName();

        this.viewHelp = extractMainResponseData.getViewHelp();

        this.infoRequestDetail = new InfoRequestDetail(extractMainResponseData);

        this.client = ClientVer4.fromPayerXml(extractMainResponseData.getPayer());

        this.infoExtractDetail = new InfoExtractDetail(extractMainResponseData);
    }
}
