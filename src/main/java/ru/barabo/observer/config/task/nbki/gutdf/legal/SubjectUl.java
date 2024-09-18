package ru.barabo.observer.config.task.nbki.gutdf.legal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectEventDataFL;
import ru.barabo.observer.config.task.nbki.gutdf.physic.title.SubjectTitleDataFl;

/**
 * Сведения о субъекте - юридическом лице
 */
@XStreamAlias("Subject_UL")
public class SubjectUl {

    @XStreamAlias("Title")
    private final SubjectTitleDataUl title;

    @XStreamAlias("Events")
    private final SubjectEventDataUl events;


    public SubjectUl(SubjectTitleDataUl title, SubjectEventDataUl events) {

        this.title = title;

        this.events = events;
    }

}
