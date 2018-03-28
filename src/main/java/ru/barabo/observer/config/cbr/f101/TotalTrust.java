package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ИтогоДоверит")
public class TotalTrust {

    public List<Trust> getTrustList() {
        return trustList;
    }

    @XStreamImplicit(itemFieldName="ИтогоДовУпр")
    private List<Trust> trustList;
}
