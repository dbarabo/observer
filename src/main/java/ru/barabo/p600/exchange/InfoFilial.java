package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("СведОФил")
public class InfoFilial {

    @XStreamAlias("НомФл")
    private String filialNumber = "";

    @XStreamImplicit(itemFieldName = "СведОбОпер")
    private List<InfoOper> infoOperList;

    public InfoFilial(List<Object[]> data) {
        infoOperList = new ArrayList<>();

        for(Object[] row : data) {
            infoOperList.add(new InfoOper(row));
        }
    }
}
