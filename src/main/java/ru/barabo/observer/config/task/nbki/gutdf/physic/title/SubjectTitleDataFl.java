package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Title")
public class SubjectTitleDataFl {

    @XStreamAlias("FL_1_4_Group")
    private final Fl1_4Group fl1_4Group;

    @XStreamAlias("FL_2_5_Group")
    private final Fl2_5Group fl2_5Group;

    @XStreamAlias("FL_3_Birth")
    private final Fl3Birth fl3Birth;

    @XStreamAlias("FL_6_Tax")
    private final Fl6Tax fl6Tax; // minOccurs="0"

    @XStreamAlias("FL_7_Social")
    private final Fl7Social fl7Social;  // minOccurs="0"

    public SubjectTitleDataFl(Fl1_4Group fl14Group, Fl2_5Group fl25Group, Fl3Birth fl3Birth, Fl6Tax fl6Tax, Fl7Social fl7Social) {
        fl1_4Group = fl14Group;
        fl2_5Group = fl25Group;
        this.fl3Birth = fl3Birth;
        this.fl6Tax = fl6Tax;
        this.fl7Social = fl7Social;
    }

    public String getUnical() {

        return (fl6Tax == null) || (fl6Tax.getTax() == null) || fl6Tax.getTax().length() < 10
                ? getPassport() : fl6Tax.getTax();
    }

    private String getPassport() {
        return fl1_4Group.getFl4Doc().getDocSeries().value + fl1_4Group.getFl4Doc().getDocNum().value;
    }

    public Fl1_4Group getFl1_4Group() {
        return fl1_4Group;
    }

    public Fl2_5Group getFl2_5Group() {
        return fl2_5Group;
    }

    public Fl3Birth getFl3Birth() {
        return fl3Birth;
    }

    public Fl6Tax getFl6Tax() {
        return fl6Tax;
    }

    public Fl7Social getFl7Social() {
        return fl7Social;
    }
}
