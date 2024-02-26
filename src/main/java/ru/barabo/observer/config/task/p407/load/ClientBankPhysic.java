package ru.barabo.observer.config.task.p407.load;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФизЛицо")
public class ClientBankPhysic {

    @XStreamAlias("ФЛФИО")
    private PhysicFio fio;

    @XStreamAlias("ФЛИНН")
    private String inn;

    @XStreamAlias("ИПОГРНИП")
    private String ogrn;

    @XStreamAlias("ФЛДатаРожд")
    private String birthday;

    @XStreamAlias("ПризнакВнимания")
    private Integer isAttention;

    public PhysicFio getFio() {
        return fio;
    }

    public String getInn() {
        return inn;
    }

}
