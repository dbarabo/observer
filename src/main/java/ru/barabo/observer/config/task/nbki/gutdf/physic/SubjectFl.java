package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.SubjectTitleDataFl;

@XStreamAlias("Subject_FL")
public class SubjectFl {

    @XStreamAlias("Title")
    private final SubjectTitleDataFl title;

    @XStreamAlias("Events")
    private final SubjectEventDataFL events;


    public SubjectFl(SubjectTitleDataFl title, SubjectEventDataFL events) {

        this.title = title;

        this.events = events;
    }
}
