package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Р4.14")
public class SubSectionR414 {

    @XStreamAlias("Р4.14_2")
    final private String idSubjectCode;

    @XStreamAlias("Р4.14_3")
    final private String authorizedCapitalPercent;

    public SubSectionR414(String idSubjectCode, Number authorizedCapitalPercent) {

        this.idSubjectCode = idSubjectCode;

        this.authorizedCapitalPercent = XmlLoader.formatDecimal3(authorizedCapitalPercent);
    }
}
