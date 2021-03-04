package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

public class GeneralInfoPartVer4 {

    @XStreamAlias("НомСправ")
    private String numberHelp;

    @XStreamAlias("НомВыпис")
    private String numberExtract;

    @XStreamAlias("ДатаСправ")
    private String dateHelp;

    @XStreamAlias("ДатаВыпис")
    private String dateExtract;

    @XStreamAlias("ТипСправ")
    private String typeHelp;

    @XStreamAlias("ТипВыпис")
    private String typeExtract;

    protected GeneralInfoPartVer4(boolean isExtract) {
        if(isExtract) {
            numberExtract = (System.currentTimeMillis() / 100) % 10000000 + "";

            dateExtract = XmlLoader.formatDate(new Date());

            typeExtract = "1";
        } else {
            numberHelp = (System.currentTimeMillis() / 100) % 10000000 + "";

            dateHelp = XmlLoader.formatDate(new Date());

            typeHelp = "1";
        }
    }
}
