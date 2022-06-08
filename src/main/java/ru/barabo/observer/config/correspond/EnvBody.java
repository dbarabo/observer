package ru.barabo.observer.config.correspond;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("env:Body")
public class EnvBody {

    @XStreamAlias("sen:SigEnvelope")
    private ContainerBase64 containerBase64;

    public ContainerBase64 getContainerBase64() {
        return containerBase64;
    }
}
