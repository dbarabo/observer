package ru.barabo.observer.config.task.nbki.gutdf.physic.block.current;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl55Application;
import ru.barabo.observer.config.task.nbki.gutdf.physic.block.Fl57Reject;

public class Fl55_57Group {

    @XStreamAlias("FL_55_Application")
    private final Fl55Application fl55Application;

    @XStreamAlias("FL_57_Reject")
    private final Fl57Reject fl57Reject;

    public Fl55_57Group(Fl55Application fl55Application, Fl57Reject fl57Reject) {
        this.fl55Application = fl55Application;
        this.fl57Reject = fl57Reject;
    }

    public Fl55Application getFl55Application() {
        return fl55Application;
    }

    public Fl57Reject getFl57Reject() {
        return fl57Reject;
    }
}
