package ru.barabo.observer.config.correspond;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("env:Envelope")
public class ContainerEnvEnvelope {

    @XStreamOmitField
    @XStreamAlias("env:Header")
    private Object ignoredElement;

    @XStreamAlias("env:Body")
    private EnvBody envBody;

    public EnvBody getEnvBody() {
        return envBody;
    }
}
