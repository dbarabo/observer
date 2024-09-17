package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.*;

import java.util.List;

@XStreamAlias("Events")
public class SubjectEventDataFL {

    @XStreamImplicit(itemFieldName = "FL_Event_1_1")
    private final List<FlEvent1_1> flEvent1_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_2")
    private final List<FlEvent1_2> flEvent1_2List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_3")
    private final List<FlEvent1_3> flEvent1_3List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_4")
    private final List<FlEvent1_4> flEvent1_4List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_7")
    private final List<FlEvent1_7> flEvent1_7List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_9")
    private final List<FlEvent1_9> flEvent1_9List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_12")
    private final List<FlEvent1_12> flEvent1_12List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_1")
    private final List<FlEvent2_1> flEvent2_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_2")
    private final List<FlEvent2_2> flEvent2_2List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_2_1")
    private final List<FlEvent2_2_1> flEvent2_2_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_3")
    private final List<FlEvent2_3> flEvent2_3List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_4")
    private final List<FlEvent2_4> flEvent2_4List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_5")
    private final List<FlEvent2_5> flEvent2_5List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_6")
    private final List<FlEvent2_6> flEvent2_6List;

    public SubjectEventDataFL(List<FlEvent1_1> flEvent1_1List, List<FlEvent1_2> flEvent1_2List,
                              List<FlEvent1_3> flEvent1_3List, List<FlEvent1_4> flEvent1_4List,
                              List<FlEvent1_7> flEvent1_7List, List<FlEvent1_9> flEvent1_9List,
                              List<FlEvent1_12> flEvent1_12List,
                              List<FlEvent2_1> flEvent2_1List, List<FlEvent2_2> flEvent2_2List,
                              List<FlEvent2_2_1> flEvent2_2_1List, List<FlEvent2_3> flEvent2_3List,
                              List<FlEvent2_4> flEvent2_4List, List<FlEvent2_5> flEvent2_5List,
                              List<FlEvent2_6> flEvent2_6List) {

        this.flEvent1_1List = flEvent1_1List;

        this.flEvent1_2List = flEvent1_2List;

        this.flEvent1_3List = flEvent1_3List;

        this.flEvent1_4List = flEvent1_4List;

        this.flEvent1_7List = flEvent1_7List;

        this.flEvent1_9List = flEvent1_9List;

        this.flEvent1_12List = flEvent1_12List;


        this.flEvent2_1List = flEvent2_1List;

        this.flEvent2_2List = flEvent2_2List;

        this.flEvent2_2_1List = flEvent2_2_1List;

        this.flEvent2_3List = flEvent2_3List;

        this.flEvent2_4List = flEvent2_4List;

        this.flEvent2_5List = flEvent2_5List;

        this.flEvent2_6List = flEvent2_6List;
    }
}
