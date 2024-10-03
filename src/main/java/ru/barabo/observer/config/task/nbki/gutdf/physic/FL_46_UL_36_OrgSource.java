package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

@XStreamAlias("FL_46_UL_36_OrgSource")
public class FL_46_UL_36_OrgSource {

    @XStreamAlias("sourceCode")
    final private StringElement sourceCode = new StringElement("1");

    @XStreamAlias("sourceRegistrationFact_1")
    final private StringElement sourceRegistrationFact1 = new StringElement("");

    @XStreamAlias("fullName")
    final private StringElement fullName =
            new StringElement("Общество с ограниченной ответственностью \"Примтеркомбанк\"");

    @XStreamAlias("regNum")
    final private StringElement regNum = new StringElement("1022500001325");

    @XStreamAlias("sourceCreditInfoDate")
    final private StringElement sourceCreditInfoDate;

    public FL_46_UL_36_OrgSource(GutdfData gutdfData) {

        sourceCreditInfoDate = new StringElement( XmlLoader.formatDate(gutdfData.dateDocument() ) );
    }
}
