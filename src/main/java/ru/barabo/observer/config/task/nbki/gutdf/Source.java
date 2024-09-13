package ru.barabo.observer.config.task.nbki.gutdf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData;
import ru.barabo.observer.config.task.nbki.gutdf.physic.FL_46_UL_36_OrgSource;

@XStreamAlias("Source")
public class Source {

    @XStreamAlias("FL_46_UL_36_OrgSource")
    final private FL_46_UL_36_OrgSource fL46Ul36OrgSource;

    public Source(GutdfData gutdfData) {
        fL46Ul36OrgSource = new FL_46_UL_36_OrgSource(gutdfData);
    }
}
