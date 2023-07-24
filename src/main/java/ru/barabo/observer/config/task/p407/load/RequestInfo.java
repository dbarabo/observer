package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ВидЗапрИнфо")
public class RequestInfo {

    @XStreamAlias("ЭЛ04ЗапрВыписок")
    private ExtractRequestInfo extractRequestInfo;

    public ExtractRequestInfo getExtractRequestInfo() {
        return extractRequestInfo;
    }
}
