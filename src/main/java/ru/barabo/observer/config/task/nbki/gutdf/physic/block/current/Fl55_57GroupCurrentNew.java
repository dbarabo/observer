package ru.barabo.observer.config.task.nbki.gutdf.physic.block.current;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Fl55_57GroupCurrentNew {

    @XStreamAlias("FL_55_57_Group_current")
    private final Fl55_57Group fl55_57GroupCurrent;

    @XStreamAlias("FL_55_57_Group_new")
    private final Fl55_57Group fl55_57GroupNew;

    public Fl55_57GroupCurrentNew(Fl55_57Group fl5557GroupCurrent, Fl55_57Group fl5557GroupNew) {
        this.fl55_57GroupCurrent = fl5557GroupCurrent;
        this.fl55_57GroupNew = fl5557GroupNew;
    }

    public Fl55_57Group getFl55_57GroupCurrent() {
        return fl55_57GroupCurrent;
    }

    public Fl55_57Group getFl55_57GroupNew() {
        return fl55_57GroupNew;
    }
}
