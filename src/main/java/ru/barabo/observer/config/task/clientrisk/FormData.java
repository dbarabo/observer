package ru.barabo.observer.config.task.clientrisk;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.ed711497.InfoPercent;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;
import java.util.List;

@XStreamAlias("Form_Data")
public class FormData {

    @XStreamAsAttribute
    @XStreamAlias("Report_Date")
    final String reportDate = XmlLoader.formatDate(new Date());

    @XStreamAsAttribute
    @XStreamAlias("INN")
    final String innBank = "2540015598";

    @XStreamAsAttribute
    @XStreamAlias("OGRN")
    final String ogrnBank = "1022500001325";

    @XStreamAsAttribute
    @XStreamAlias("FullName")
    final String nameBank = "Общество с ограниченной ответственностью \"Примтеркомбанк\"";

    @XStreamAsAttribute
    @XStreamAlias("RegNo")
    final String regNo = "0021";

    @XStreamImplicit(itemFieldName = "Client")
    final List<ClientForm> clientForms;

    public FormData(List<ClientForm> clientForms) {

        this.clientForms = clientForms;
    }
}
