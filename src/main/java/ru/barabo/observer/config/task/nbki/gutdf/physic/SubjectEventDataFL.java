package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.jetbrains.annotations.NotNull;
import ru.barabo.observer.config.task.nbki.gutdf.physic.event.*;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("Events")
public class SubjectEventDataFL {

    @XStreamImplicit(itemFieldName = "FL_Event_1_1")
    private List<FlEvent1_1> flEvent1_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_12")
    private List<FlEvent1_12> flEvent1_12List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_2")
    private List<FlEvent1_2> flEvent1_2List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_3")
    private List<FlEvent1_3> flEvent1_3List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_4")
    private List<FlEvent1_4> flEvent1_4List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_7")
    private List<FlEvent1_7> flEvent1_7List;

    @XStreamImplicit(itemFieldName = "FL_Event_1_9")
    private List<FlEvent1_9> flEvent1_9List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_1")
    private List<FlEvent2_1> flEvent2_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_2")
    private List<FlEvent2_2> flEvent2_2List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_2_1")
    private List<FlEvent2_2_1> flEvent2_2_1List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_3")
    private List<FlEvent2_3> flEvent2_3List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_4")
    private List<FlEvent2_4> flEvent2_4List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_5")
    private List<FlEvent2_5> flEvent2_5List;

    @XStreamImplicit(itemFieldName = "FL_Event_2_6")
    private List<FlEvent2_6> flEvent2_6List;

    @XStreamImplicit(itemFieldName = "FL_Event_3_1")
    private List<FlEvent3_1> flEvent3_1List;

    public SubjectEventDataFL() {

        this.flEvent1_1List = null;
        this.flEvent1_2List = null;
        this.flEvent1_3List = null;
        this.flEvent1_4List = null;
        this.flEvent1_7List = null;
        this.flEvent1_9List = null;
        this.flEvent1_12List = null;
        this.flEvent2_1List = null;
        this.flEvent2_2List = null;
        this.flEvent2_2_1List = null;
        this.flEvent2_3List = null;
        this.flEvent2_4List = null;
        this.flEvent2_5List = null;
        this.flEvent2_6List = null;
        this.flEvent3_1List = null;
    }

    public List<FlEvent1_1> getFlEvent1_1List() {
        return flEvent1_1List;
    }

    public List<FlEvent1_2> getFlEvent1_2List() {
        return flEvent1_2List;
    }

    public List<FlEvent1_3> getFlEvent1_3List() {
        return flEvent1_3List;
    }

    public List<FlEvent1_4> getFlEvent1_4List() {
        return flEvent1_4List;
    }

    public List<FlEvent1_7> getFlEvent1_7List() {
        return flEvent1_7List;
    }

    public List<FlEvent1_9> getFlEvent1_9List() {
        return flEvent1_9List;
    }

    public List<FlEvent1_12> getFlEvent1_12List() {
        return flEvent1_12List;
    }

    public List<FlEvent2_1> getFlEvent2_1List() {
        return flEvent2_1List;
    }

    public List<FlEvent2_2> getFlEvent2_2List() {
        return flEvent2_2List;
    }

    public List<FlEvent2_2_1> getFlEvent2_2_1List() {
        return flEvent2_2_1List;
    }

    public List<FlEvent2_3> getFlEvent2_3List() {
        return flEvent2_3List;
    }

    public List<FlEvent2_4> getFlEvent2_4List() {
        return flEvent2_4List;
    }

    public List<FlEvent2_5> getFlEvent2_5List() {
        return flEvent2_5List;
    }

    public List<FlEvent2_6> getFlEvent2_6List() {
        return flEvent2_6List;
    }

    public List<FlEvent3_1> getFlEvent3_1List() {
        return flEvent3_1List;
    }

    public void addEvent1_1(@NotNull FlEvent1_1 event1_1) {

        if(flEvent1_1List == null) {
            flEvent1_1List = new ArrayList<>();
        }
        flEvent1_1List.add(event1_1);
    }

    public void addEvent1_2(@NotNull FlEvent1_2 event1_2) {

        if(flEvent1_2List == null) {
            flEvent1_2List = new ArrayList<>();
        }
        flEvent1_2List.add(event1_2);
    }

    public void addEvent1_3(@NotNull FlEvent1_3 event1_3) {

        if(flEvent1_3List == null) {
            flEvent1_3List = new ArrayList<>();
        }
        flEvent1_3List.add(event1_3);
    }

    public void addEvent1_4(@NotNull FlEvent1_4 event1_4) {

        if(flEvent1_4List == null) {
            flEvent1_4List = new ArrayList<>();
        }
        flEvent1_4List.add(event1_4);
    }

    public void addEvent1_7(@NotNull FlEvent1_7 event1_7) {

        if(flEvent1_7List == null) {
            flEvent1_7List = new ArrayList<>();
        }
        flEvent1_7List.add(event1_7);
    }

    public void addEvent1_9(@NotNull FlEvent1_9 event1_9) {

        if(flEvent1_9List == null) {
            flEvent1_9List = new ArrayList<>();
        }
        flEvent1_9List.add(event1_9);
    }

    public void addEvent1_12(@NotNull FlEvent1_12 event1_12) {

        if(flEvent1_12List == null) {
            flEvent1_12List = new ArrayList<>();
        }
        flEvent1_12List.add(event1_12);
    }

    public void addEvent2_1(@NotNull FlEvent2_1 event2_1) {

        if(flEvent2_1List == null) {
            flEvent2_1List = new ArrayList<>();
        }
        flEvent2_1List.add(event2_1);
    }

    public void addEvent2_2(@NotNull FlEvent2_2 event2_2) {

        if(flEvent2_2List == null) {
            flEvent2_2List = new ArrayList<>();
        }
        flEvent2_2List.add(event2_2);
    }

    public void addEvent2_2_1(@NotNull FlEvent2_2_1 event2_2_1) {

        if(flEvent2_2_1List == null) {
            flEvent2_2_1List = new ArrayList<>();
        }
        flEvent2_2_1List.add(event2_2_1);
    }

    public void addEvent2_3(@NotNull FlEvent2_3 event2_3) {

        if(flEvent2_3List == null) {
            flEvent2_3List = new ArrayList<>();
        }
        flEvent2_3List.add(event2_3);
    }

    public void addEvent2_4(@NotNull FlEvent2_4 event2_4) {

        if(flEvent2_4List == null) {
            flEvent2_4List = new ArrayList<>();
        }
        flEvent2_4List.add(event2_4);
    }

    public void addEvent2_5(@NotNull FlEvent2_5 event2_5) {

        if(flEvent2_5List == null) {
            flEvent2_5List = new ArrayList<>();
        }
        flEvent2_5List.add(event2_5);
    }

    public void addEvent2_6(@NotNull FlEvent2_6 event2_6) {

        if(flEvent2_6List == null) {
            flEvent2_6List = new ArrayList<>();
        }
        flEvent2_6List.add(event2_6);
    }

    public void addEvent3_1(@NotNull FlEvent3_1 event3_1) {

        if(flEvent3_1List == null) {
            flEvent3_1List = new ArrayList<>();
        }
        flEvent3_1List.add(event3_1);
    }
}
