package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.SubjectTitleDataFl;

@XStreamAlias("Subject_FL")
public class SubjectFl {

    @XStreamAlias("Title")
    private final SubjectTitleDataFl title;

    @XStreamAlias("Events")
    private final SubjectEventDataFL events;

    transient private final Long idClient;

    public SubjectFl(Long idClient, SubjectTitleDataFl title, SubjectEventDataFL events) {

        this.idClient = idClient;

        this.title = title;

        this.events = events;
    }

    public Long getIdClient() {
        return idClient;
    }

    public SubjectEventDataFL getEvents() {
        return events;
    }
}
