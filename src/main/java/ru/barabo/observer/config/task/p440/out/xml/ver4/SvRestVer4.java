package ru.barabo.observer.config.task.p440.out.xml.ver4;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("СвОстат")
public class SvRestVer4 {
    @XStreamImplicit(itemFieldName = "Остат")
    private List<RestVer4> restVer4;

    public SvRestVer4(Date restDate, Number rest) {
        restVer4 = new ArrayList<RestVer4>();

        restVer4.add(new RestVer4(restDate, rest) );
    }
}
