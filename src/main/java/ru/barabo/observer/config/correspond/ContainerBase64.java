package ru.barabo.observer.config.correspond;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("sen:SigEnvelope")
public class ContainerBase64 {

    @XStreamOmitField
    @XStreamAlias("sen:SigContainer")
    private Object ignoredElement;

    @XStreamAlias("sen:Object")
    private String objectBase64;

    public String getObjectBase64() {
        return objectBase64;
    }
}
