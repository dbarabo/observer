package ru.barabo.observer.config.task.nbki.gutdf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.SubjectFl;

import java.util.List;

@XStreamAlias("Data")
public class Data {

    @XStreamImplicit(itemFieldName = "Subject_FL")
    private final List<SubjectFl> subjectFlList;

    @XStreamImplicit(itemFieldName = "Subject_UL")
    private final List<SubjectUl> subjectUlList;

    public Data(GutdfData gutdfData) {

        subjectFlList = gutdfData.subjectFlList();

        subjectUlList = gutdfData.subjectUlList();
    }
}
