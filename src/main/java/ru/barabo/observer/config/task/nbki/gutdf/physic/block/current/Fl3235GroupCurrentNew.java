package ru.barabo.observer.config.task.nbki.gutdf.physic.block.current;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl32_35Group;

public class Fl3235GroupCurrentNew {

    @XStreamAlias("FL_32_35_Group_current")
    private final Fl32_35Group fl3235GroupCurrent;

    @XStreamAlias("FL_32_35_Group_new")
    private final Fl32_35Group fl3235GroupNew;

    public Fl3235GroupCurrentNew(Fl32_35Group fl3235GroupCurrent, Fl32_35Group fl3235GroupNew) {
        this.fl3235GroupCurrent = fl3235GroupCurrent;
        this.fl3235GroupNew = fl3235GroupNew;
    }

    public Fl32_35Group getFl55_57GroupCurrent() {
        return fl3235GroupCurrent;
    }

    public Fl32_35Group getFl55_57GroupNew() {
        return fl3235GroupNew;
    }
}
