package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.barabo.p440.out.data.AbstractRequestResponse;
import ru.barabo.observer.config.barabo.p440.out.data.ExistsResponseData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("СведЗапр")
public class InfoRequestDetail {

    @XStreamAlias("НомЗапр")
    private String numberRequest;

    @XStreamAlias("ДатаЗапр")
    private String dateRequest;

    @XStreamAlias("КодНОЗапр")
    private String codeFns;

    @XStreamAlias("ИндЗапр")
    private String idRequestFns;

    @XStreamAlias("НаимПодтв")
    private String pbFileBnsOnly;

    @XStreamAlias("ВидСпр")
    private String viewHelpBnsOnly;

    public InfoRequestDetail(AbstractRequestResponse request) {
        numberRequest = request.getNumberRequest();

        dateRequest = XmlLoader.formatDate(request.getDateRequest());

        codeFns = request.getFns().getFnsCode();

        idRequestFns = request.getIdRequest();

        if(request instanceof ExistsResponseData) {
            pbFileBnsOnly = request.getPbFileName();

            viewHelpBnsOnly = ((ExistsResponseData)request).getViewHelp();
        }
    }
}
