package ru.barabo.observer.config.task.nbki.gutdf.legal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Сведения о субъекте - юридическом лице
 */
@XStreamAlias("Subject_UL")
public class SubjectUl {

    @XStreamAlias("Title")
    private final SubjectTitleDataUl title;

    @XStreamAlias("Events")
    private final SubjectEventDataUl events;

    transient private final Long idClient;

    public SubjectUl(Long idClient, SubjectTitleDataUl title, SubjectEventDataUl events) {

        this.idClient = idClient;

        this.title = title;

        this.events = events;

    }

    public Long getIdClient() {
        return idClient;
    }

    public SubjectEventDataUl getEvents() {
        return events;
    }

    public SubjectTitleDataUl getTitle() {
        return title;
    }
}
