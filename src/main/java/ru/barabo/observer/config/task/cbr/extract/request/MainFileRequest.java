package ru.barabo.observer.config.task.cbr.extract.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Файл")
public class MainFileRequest {

    @XStreamAlias("ИдЭС")
    public String guid;

    @XStreamAlias("ВерсПрог")
    public String versionProgram;

    @XStreamAlias("ВерсФорм")
    public String versionForm;

    @XStreamAlias("ФайлЗапросВКО")
    public RequestVko requestVko;

}
