package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("СвБанк")
public final class SvBank {

    @XStreamAlias("РегНом")
    private final Integer registerNumber = 21;

    @XStreamAlias("НомФ")
    private final String filial = "0";

    @XStreamAlias("БИК")
    private final String bik = "040507717";

    @XStreamAlias("НаимКО")
    private final String name = "Общество с ограниченной ответственностью \"Примтеркомбанк\"";

    @XStreamAlias("ИННКО")
    private final String inn = "2540015598";

    @XStreamAlias("КППКО")
    private final String kpp = "254001001";

    @XStreamAlias("ОГРНКО")
    private final String ogrn = "1022500001325";
}
