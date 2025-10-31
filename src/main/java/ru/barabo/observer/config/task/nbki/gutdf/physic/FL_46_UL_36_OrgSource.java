package ru.barabo.observer.config.task.nbki.gutdf.physic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.title.TaxNumGroupUl4Tax;
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
            new StringElement("Общество с ограниченной ответственностью \"Примтеркомбанк\"".toUpperCase());

    @XStreamAlias("shortName")
    final private StringElement shortName =
            new StringElement("ООО \"Примтеркомбанк\"".toUpperCase());

    @XStreamAlias("regNum")
    final private StringElement regNum = new StringElement("1022500001325");

    @XStreamAlias("TaxNum_group_FL_46_UL_36_OrgSource")
    final private TaxNumGroupUl4Tax inn = new TaxNumGroupUl4Tax("2540015598");

    @XStreamAlias("sourceCreditInfoDate")
    final private StringElement sourceCreditInfoDate;

    public FL_46_UL_36_OrgSource(GutdfData gutdfData) {

        sourceCreditInfoDate = new StringElement( XmlLoader.formatDate(gutdfData.dateDocument() ) );
    }
}
