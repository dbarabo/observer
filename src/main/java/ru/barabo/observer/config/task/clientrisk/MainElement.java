package ru.barabo.observer.config.task.clientrisk;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@XStreamAlias("Form")
public class MainElement {

    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-ru:e-forms-mf-form-KYC:v0.0.10";

    @XStreamAsAttribute
    @XStreamAlias("xsi:schemaLocation")
    final String schemaLocation = "urn:cbr-ru:e-forms-mf-form-KYC:v0.0.10 schema.xsd";

    @XStreamAsAttribute
    @XStreamAlias("xmlns:xsi")
    final String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

    @XStreamAsAttribute
    @XStreamAlias("UniqueIdentifierReport")
    final String guid = UUID.randomUUID().toString().toUpperCase();

    @XStreamAsAttribute
    @XStreamAlias("CodeReport")
    final Integer codeReport;

    @XStreamAsAttribute
    @XStreamAlias("DateTimeFormationReport")
    final String reportDateTime = XmlLoader.formatDateTime(new Date());

    @XStreamAlias("Form_Data")
    final FormData formData;

    public MainElement(List<ClientForm> clientForms, Integer codeReport) {

        this.codeReport = codeReport;

        formData = new FormData(clientForms);
    }
}
