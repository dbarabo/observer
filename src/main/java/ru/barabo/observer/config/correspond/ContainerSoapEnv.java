package ru.barabo.observer.config.correspond;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("soapenv:Envelope")
public class ContainerSoapEnv {

    @XStreamOmitField
    @XStreamAlias("soapenv:Header")
    private Object ignoredElement;

    @XStreamAlias("soapenv:Body")
    private Object emptyBody;

    public ContainerBase64 emptyContainer() {
        return ContainerBase64.emptyContainer();
    }
}
