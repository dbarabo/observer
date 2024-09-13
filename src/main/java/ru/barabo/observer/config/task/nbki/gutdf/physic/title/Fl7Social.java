package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("FL_7_Social")
public class Fl7Social {

    @XStreamAlias("socialNum")
    private final StringElement socialNum;

    public Fl7Social(String socialNum) {

        this.socialNum = socialNum == null ? null : new StringElement(socialNum);
    }
}
