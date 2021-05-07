package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Р1.4")
public class SubSectionR14 {

    @XStreamAlias("Р1.4_2")
    final private String dateClosePledge;

    public  SubSectionR14(Date closePledge) {

        this.dateClosePledge = XmlLoader.formatDate( closePledge );
    }
}
