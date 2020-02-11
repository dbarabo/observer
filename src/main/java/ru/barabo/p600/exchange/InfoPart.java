package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

@XStreamAlias("ИнфЧасть")
public class InfoPart {

    @XStreamAlias("НаимКО")
    private String bankName = "ООО ПРИМТЕРКОМБАНК";

    @XStreamAlias("РегНомКО")
    private String regNumberBank = "21";

    @XStreamAlias("СведОФил")
    private InfoFilial infoFilial;

    public InfoPart(List<Object[]> data) {
        infoFilial = new InfoFilial(data);
    }
}
