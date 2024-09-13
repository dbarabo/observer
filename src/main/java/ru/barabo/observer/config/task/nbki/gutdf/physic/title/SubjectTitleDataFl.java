package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Title")
public class SubjectTitleDataFl {

    @XStreamAlias("FL_1_Name")
    private final Fl1Name fio;

    @XStreamAlias("FL_2_PrevName")
    private final Fl2PrevName flPrevName;

    @XStreamAlias("FL_3_Birth")
    private final Fl3Birth fl3Birth;

    @XStreamAlias("FL_4_Doc")
    private final Fl4Doc fl4Doc;

    @XStreamAlias("FL_5_PrevDoc")
    private final Fl5PrevDoc fl5PrevDoc;

    @XStreamAlias("FL_6_Tax")
    private final Fl6Tax fl6Tax;

    @XStreamAlias("FL_7_Social")
    private final Fl7Social fl7Social;

    public SubjectTitleDataFl(Fl1Name fio, Fl2PrevName flPrevName, Fl3Birth fl3Birth, Fl4Doc fl4Doc,
                              Fl5PrevDoc fl5PrevDoc, Fl6Tax fl6Tax, Fl7Social fl7Social) {

        this.fio = fio;
        this.flPrevName = flPrevName;
        this.fl3Birth = fl3Birth;
        this.fl4Doc = fl4Doc;
        this.fl5PrevDoc = fl5PrevDoc;
        this.fl6Tax = fl6Tax;
        this.fl7Social = fl7Social;
    }
}
