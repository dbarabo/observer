package ru.barabo.observer.config.task.nbki.gutdf.legal;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.nbki.gutdf.legal.event.*;

import java.util.List;

/**
 * События
 */
public class SubjectEventDataUl {

    @XStreamImplicit(itemFieldName = "UL_Event_1_1")
    private final List<UlEvent1_1> ulEvent1_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_2")
    private final List<UlEvent1_2> ulEvent1_2List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_3")
    private final List<UlEvent1_3> ulEvent1_3List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_4")
    private final List<UlEvent1_4> ulEvent1_4List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_7")
    private final List<UlEvent1_7> ulEvent1_7List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_1")
    private final List<UlEvent2_1> ulEvent2_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_2")
    private final List<UlEvent2_2> ulEvent2_2List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_2_1")
    private final List<UlEvent2_2_1> ulEvent2_2_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_3")
    private final List<UlEvent2_3> ulEvent2_3List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_4")
    private final List<UlEvent2_4> ulEvent2_4List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_5")
    private final List<UlEvent2_5> ulEvent2_5List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_6")
    private final List<UlEvent2_6> ulEvent2_6List;

    public SubjectEventDataUl(List<UlEvent1_1> ulEvent11List, List<UlEvent1_2> ulEvent12List,
                              List<UlEvent1_3> ulEvent13List, List<UlEvent1_4> ulEvent14List,
                              List<UlEvent1_7> ulEvent17List, List<UlEvent2_1> ulEvent21List,
                              List<UlEvent2_2> ulEvent22List, List<UlEvent2_2_1> ulEvent221List,
                              List<UlEvent2_3> ulEvent23List, List<UlEvent2_4> ulEvent24List,
                              List<UlEvent2_5> ulEvent25List, List<UlEvent2_6> ulEvent26List) {

        ulEvent1_1List = ulEvent11List;
        ulEvent1_2List = ulEvent12List;
        ulEvent1_3List = ulEvent13List;
        ulEvent1_4List = ulEvent14List;
        ulEvent1_7List = ulEvent17List;
        ulEvent2_1List = ulEvent21List;
        ulEvent2_2List = ulEvent22List;
        ulEvent2_2_1List = ulEvent221List;
        ulEvent2_3List = ulEvent23List;
        ulEvent2_4List = ulEvent24List;
        ulEvent2_5List = ulEvent25List;
        ulEvent2_6List = ulEvent26List;
    }
}
