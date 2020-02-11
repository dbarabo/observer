package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Date;
import java.util.List;

@XStreamAlias("ВыпискаИзРеестраОпер")
public class ExtractRegisterOper {

    @XStreamAlias("СлужЧасть")
    private ServicePart servicePart;

    @XStreamAlias("ИнфЧасть")
    private InfoPart infoPart;

    public ExtractRegisterOper(List<Object[]> data, Date start, Date end) {

        servicePart = new ServicePart(start, end);

        infoPart = new InfoPart(data);
    }
}
