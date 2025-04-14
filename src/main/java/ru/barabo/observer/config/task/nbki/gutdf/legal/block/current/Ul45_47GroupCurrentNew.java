package ru.barabo.observer.config.task.nbki.gutdf.legal.block.current;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Ul45_47GroupCurrentNew {

    @XStreamAlias("UL_45_47_Group_current")
    private final Ul45_47Group ul45_47GroupCurrent;

    @XStreamAlias("UL_45_47_Group_new")
    private final Ul45_47Group ul45_47GroupNew;

    public Ul45_47GroupCurrentNew(Ul45_47Group ul4547GroupCurrent, Ul45_47Group ul4547GroupNew) {
        this.ul45_47GroupCurrent = ul4547GroupCurrent;
        this.ul45_47GroupNew = ul4547GroupNew;
    }

    public Ul45_47Group getUl45_47GroupNew() {
        return ul45_47GroupNew;
    }

    public Ul45_47Group getUl45_47GroupCurrent() {
        return ul45_47GroupCurrent;
    }
}
