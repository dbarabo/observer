package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

@XStreamAlias("FL_4_Doc")
public class Fl4Doc {

    @XStreamAlias("countryCode")
    private final StringElement countryCode = new StringElement("643");

    @XStreamAlias("docCode")
    private final StringElement docCode;

    @XStreamAlias("docSeries")
    private final StringElement docSeries;

    @XStreamAlias("docNum")
    private final StringElement docNum;

    @XStreamAlias("issueDate")
    private final StringElement issueDate;

    @XStreamAlias("docIssuer")
    private final StringElement docIssuer; // 4.8. Кем выдан документ

    @XStreamAlias("deptCode")
    private final StringElement deptCode;

    @XStreamAlias("foreignerCode")
    private final StringElement foreignerCode; // 4.11. Признак иностранного гражданина

    public Fl4Doc(String docCode, String docSeries, String docNum, Date issueDate,
                  String docIssuer, String deptCode, String foreignerCode) {

        this.docCode = new StringElement(docCode);

        this.docSeries = docSeries == null ? null : new StringElement(docSeries);

        this.docNum = new StringElement(docNum);

        this.issueDate = new StringElement( XmlLoader.formatDate(issueDate) );

        this.docIssuer = docIssuer == null ? null : new StringElement(docIssuer);

        this.deptCode = deptCode == null ? null : new StringElement(deptCode);

        this.foreignerCode = new StringElement(foreignerCode);
    }

    public StringElement getDocCode() {
        return docCode;
    }

    public StringElement getDocSeries() {
        return docSeries;
    }

    public StringElement getDocNum() {
        return docNum;
    }

    public StringElement getIssueDate() {
        return issueDate;
    }

    public StringElement getDocIssuer() {
        return docIssuer;
    }

    public StringElement getDeptCode() {
        return deptCode;
    }

    public StringElement getForeignerCode() {
        return foreignerCode;
    }
}
