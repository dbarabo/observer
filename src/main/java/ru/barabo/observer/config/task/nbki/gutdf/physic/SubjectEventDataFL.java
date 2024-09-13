package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.*;

import java.util.List;

@XStreamAlias("Events")
public class SubjectEventDataFL {

    @XStreamImplicit(itemFieldName = "FL_event_1.1")
    private final List<FlEvent1_1> flEvent1_1List;

    @XStreamImplicit(itemFieldName = "FL_event_1.2")
    private final List<FlEvent1_2> flEvent1_2List;

    @XStreamImplicit(itemFieldName = "FL_event_1.3")
    private final List<FlEvent1_3> flEvent1_3List;

    @XStreamImplicit(itemFieldName = "FL_event_1.4")
    private final List<FlEvent1_4> flEvent1_4List;

    @XStreamImplicit(itemFieldName = "FL_event_1.7")
    private final List<FlEvent1_7> flEvent1_7List;

    @XStreamImplicit(itemFieldName = "FL_event_1.9")
    private final List<FlEvent1_9> flEvent1_9List;

    @XStreamImplicit(itemFieldName = "FL_event_1.12")
    private final List<FlEvent1_12> flEvent1_12List;

    public SubjectEventDataFL(List<FlEvent1_1> flEvent1_1List, List<FlEvent1_2> flEvent1_2List,
                              List<FlEvent1_3> flEvent1_3List, List<FlEvent1_4> flEvent1_4List,
                              List<FlEvent1_7> flEvent1_7List, List<FlEvent1_9> flEvent1_9List,
                              List<FlEvent1_12> flEvent1_12List) {

        this.flEvent1_1List = flEvent1_1List;

        this.flEvent1_2List = flEvent1_2List;

        this.flEvent1_3List = flEvent1_3List;

        this.flEvent1_4List = flEvent1_4List;

        this.flEvent1_7List = flEvent1_7List;

        this.flEvent1_9List = flEvent1_9List;

        this.flEvent1_12List = flEvent1_12List;
    }
}
