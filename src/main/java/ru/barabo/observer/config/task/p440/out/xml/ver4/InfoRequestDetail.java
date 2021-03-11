package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.*;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("СведЗапр")
public class InfoRequestDetail {

    @XStreamAlias("НомЗапр")
    private String numberRequest;

    @XStreamAlias("ДатаЗапр")
    private String dateRequest;

    @XStreamAlias("КодНОЗапр")
    private String codeFnsRest;

    @XStreamAlias("КодНО")
    private String codeFnsExists;

    @XStreamAlias("ИндЗапр")
    private String idRequestFns;

    @XStreamAlias("НаимПодтв")
    private String pbFileBnsOnly;

    @XStreamAlias("ВидСпр")
    private String viewHelpBnsOnly;

    public InfoRequestDetail(AbstractRequestResponse request) {
        numberRequest = request.getNumberRequest();

        dateRequest = XmlLoader.formatDate(request.getDateRequest());

        idRequestFns = request.getIdRequest();

        if(request instanceof ExistsResponseDataVer4) {
            pbFileBnsOnly = request.getPbFileName();

            viewHelpBnsOnly = ((ExistsResponseDataVer4)request).getViewHelp();
        }

        if(request instanceof RestResponseDataVer4) {
            codeFnsRest = request.getFns().getFnsCode();
        } else {
            codeFnsExists = request.getFns().getFnsCode();
        }
    }
}
