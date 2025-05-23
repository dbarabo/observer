package ru.barabo.observer.config.task.nbki.gutdf.legal;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.jetbrains.annotations.NotNull;
import ru.barabo.observer.config.task.nbki.gutdf.legal.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * События
 */
public class SubjectEventDataUl {

    @XStreamImplicit(itemFieldName = "UL_Event_1_1")
    private List<UlEvent1_1> ulEvent1_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_2")
    private List<UlEvent1_2> ulEvent1_2List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_3")
    private List<UlEvent1_3> ulEvent1_3List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_4")
    private List<UlEvent1_4> ulEvent1_4List;

    @XStreamImplicit(itemFieldName = "UL_Event_1_7")
    private List<UlEvent1_7> ulEvent1_7List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_1")
    private List<UlEvent2_1> ulEvent2_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_2")
    private List<UlEvent2_2> ulEvent2_2List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_2_1")
    private List<UlEvent2_2_1> ulEvent2_2_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_3")
    private List<UlEvent2_3> ulEvent2_3List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_4")
    private List<UlEvent2_4> ulEvent2_4List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_5")
    private List<UlEvent2_5> ulEvent2_5List;

    @XStreamImplicit(itemFieldName = "UL_Event_2_6")
    private List<UlEvent2_6> ulEvent2_6List;

    @XStreamImplicit(itemFieldName = "UL_Event_3_1")
    private List<UlEvent3_1> ulEvent3_1List;

    @XStreamImplicit(itemFieldName = "UL_Event_3_2")
    private List<UlEvent3_2> ulEvent3_2List;

    public SubjectEventDataUl() {

        ulEvent1_1List = null;
        ulEvent1_2List = null;
        ulEvent1_3List = null;
        ulEvent1_4List = null;
        ulEvent1_7List = null;
        ulEvent2_1List = null;
        ulEvent2_2List = null;
        ulEvent2_2_1List = null;
        ulEvent2_3List = null;
        ulEvent2_4List = null;
        ulEvent2_5List = null;
        ulEvent2_6List = null;
        ulEvent3_1List = null;
    }

    public void addEvent1_1(@NotNull UlEvent1_1 event1_1) {

        if(ulEvent1_1List == null) {
            ulEvent1_1List = new ArrayList<>();
        }
        ulEvent1_1List.add(event1_1);
    }

    public void addEvent1_2(@NotNull UlEvent1_2 event1_2) {

        if(ulEvent1_2List == null) {
            ulEvent1_2List = new ArrayList<>();
        }
        ulEvent1_2List.add(event1_2);
    }

    public void addEvent1_3(@NotNull UlEvent1_3 event1_3) {

        if(ulEvent1_3List == null) {
            ulEvent1_3List = new ArrayList<>();
        }
        ulEvent1_3List.add(event1_3);
    }

    public void addEvent1_4(@NotNull UlEvent1_4 event1_4) {

        if(ulEvent1_4List == null) {
            ulEvent1_4List = new ArrayList<>();
        }
        ulEvent1_4List.add(event1_4);
    }

    public void addEvent1_7(@NotNull UlEvent1_7 event1_7) {

        if(ulEvent1_7List == null) {
            ulEvent1_7List = new ArrayList<>();
        }
        ulEvent1_7List.add(event1_7);
    }

    public void addEvent2_1(@NotNull UlEvent2_1 event2_1) {

        if(ulEvent2_1List == null) {
            ulEvent2_1List = new ArrayList<>();
        }
        ulEvent2_1List.add(event2_1);
    }

    public void addEvent2_2(@NotNull UlEvent2_2 event2_2) {

        if(ulEvent2_2List == null) {
            ulEvent2_2List = new ArrayList<>();
        }
        ulEvent2_2List.add(event2_2);
    }

    public void addEvent2_2_1(@NotNull UlEvent2_2_1 event2_2_1) {

        if(ulEvent2_2_1List == null) {
            ulEvent2_2_1List = new ArrayList<>();
        }
        ulEvent2_2_1List.add(event2_2_1);
    }

    public void addEvent2_3(@NotNull UlEvent2_3 event2_3) {

        if(ulEvent2_3List == null) {
            ulEvent2_3List = new ArrayList<>();
        }
        ulEvent2_3List.add(event2_3);
    }

    public void addEvent2_4(@NotNull UlEvent2_4 event2_4) {

        if(ulEvent2_4List == null) {
            ulEvent2_4List = new ArrayList<>();
        }
        ulEvent2_4List.add(event2_4);
    }

    public void addEvent2_5(@NotNull UlEvent2_5 event2_5) {

        if(ulEvent2_5List == null) {
            ulEvent2_5List = new ArrayList<>();
        }
        ulEvent2_5List.add(event2_5);
    }

    public void addEvent2_6(@NotNull UlEvent2_6 event2_6) {

        if(ulEvent2_6List == null) {
            ulEvent2_6List = new ArrayList<>();
        }
        ulEvent2_6List.add(event2_6);
    }

    public void addEvent3_1(@NotNull UlEvent3_1 event3_1) {

        if(ulEvent3_1List == null) {
            ulEvent3_1List = new ArrayList<>();
        }
        ulEvent3_1List.add(event3_1);
    }

    public void addEvent3_2(@NotNull UlEvent3_2 event32) {

        if(ulEvent3_2List == null) {
            ulEvent3_2List = new ArrayList<>();
        }
        ulEvent3_2List.add(event32);
    }

    public List<UlEvent1_1> getUlEvent1_1List() {
        return ulEvent1_1List;
    }

    public List<UlEvent1_2> getUlEvent1_2List() {
        return ulEvent1_2List;
    }

    public List<UlEvent1_3> getUlEvent1_3List() {
        return ulEvent1_3List;
    }

    public List<UlEvent1_4> getUlEvent1_4List() {
        return ulEvent1_4List;
    }

    public List<UlEvent1_7> getUlEvent1_7List() {
        return ulEvent1_7List;
    }

    public List<UlEvent2_1> getUlEvent2_1List() {
        return ulEvent2_1List;
    }

    public List<UlEvent2_2> getUlEvent2_2List() {
        return ulEvent2_2List;
    }

    public List<UlEvent2_2_1> getUlEvent2_2_1List() {
        return ulEvent2_2_1List;
    }

    public List<UlEvent2_3> getUlEvent2_3List() {
        return ulEvent2_3List;
    }

    public List<UlEvent2_4> getUlEvent2_4List() {
        return ulEvent2_4List;
    }

    public List<UlEvent2_5> getUlEvent2_5List() {
        return ulEvent2_5List;
    }

    public List<UlEvent2_6> getUlEvent2_6List() {
        return ulEvent2_6List;
    }

    public List<UlEvent3_2> getUlEvent3_2List() {
        return ulEvent3_2List;
    }
}
