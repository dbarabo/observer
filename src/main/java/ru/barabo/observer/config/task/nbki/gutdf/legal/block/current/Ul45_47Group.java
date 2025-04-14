package ru.barabo.observer.config.task.nbki.gutdf.legal.block.current;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul45Application;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul47Reject;

public class Ul45_47Group {

    @XStreamAlias("UL_45_Application")
    private final Ul45Application ul45Application;

    @XStreamAlias("FL_57_Reject")
    private final Ul47Reject ul47Reject;

    public Ul45_47Group(Ul45Application ul45Application, Ul47Reject ul47Reject) {
        this.ul45Application = ul45Application;
        this.ul47Reject = ul47Reject;
    }

    public Ul45Application getUl45Application() {
        return ul45Application;
    }

    public Ul47Reject getUl47Reject() {
        return ul47Reject;
    }
}
