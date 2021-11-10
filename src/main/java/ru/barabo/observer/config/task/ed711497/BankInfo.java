package ru.barabo.observer.config.task.ed711497;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tns:СведБанк")
public class BankInfo {

    @XStreamAlias("НаимБанк")
    private final String name = "Общество с ограниченной ответственностью \"Примтеркомбанк\"";

    @XStreamAlias("ИНН")
    private final String inn = "2540015598";

    @XStreamAlias("КПП")
    private final String kpp = "254001001";
}
