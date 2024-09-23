package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Fl1_4Group {

    @XStreamAlias("FL_1_Name")
    private final Fl1Name fio;

    @XStreamAlias("FL_4_Doc")
    private final Fl4Doc fl4Doc;

    public Fl1_4Group(Fl1Name fio, Fl4Doc fl4Doc) {
        this.fio = fio;
        this.fl4Doc = fl4Doc;
    }
}
