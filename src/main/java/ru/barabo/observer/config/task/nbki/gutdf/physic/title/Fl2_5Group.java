package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Fl2_5Group {

    @XStreamAlias("FL_2_PrevName")
    private final Fl2PrevName flPrevName;

    @XStreamAlias("FL_5_PrevDoc")
    private final Fl5PrevDoc fl5PrevDoc;

    public Fl2_5Group(Fl2PrevName flPrevName, Fl5PrevDoc fl5PrevDoc) {
        this.flPrevName = flPrevName;
        this.fl5PrevDoc = fl5PrevDoc;
    }
}
