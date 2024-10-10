package ru.barabo.observer.config.task.nbki.gutdf.physic.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;
import ru.barabo.observer.config.task.p440.load.xml.impl.StringElement;

import java.util.Date;

@XStreamAlias("FL_5_PrevDoc")
public class Fl5PrevDoc {

    @XStreamAlias("prevDocFact_0")
    private final StringElement prevDocFact0;

    @XStreamAlias("prevDocFact_1")
    private final StringElement prevDocFact1;

    @XStreamAlias("countryCode")
    private final StringElement countryCode;

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

    public Fl5PrevDoc() {
        prevDocFact0 = new StringElement("");

        prevDocFact1 = null;

        countryCode = null;

        docCode = null;

        docSeries = null;

        docNum = null;

        issueDate = null;

        docIssuer = null;

        deptCode = null;
    }

    public Fl5PrevDoc(String docCode, String docSeries, String docNum, Date issueDate,
                      String docIssuer, String deptCode) {
        prevDocFact0 = null;

        prevDocFact1 = new StringElement("");

        this.countryCode = new StringElement("643");

        this.docCode = new StringElement(docCode);

        this.docSeries = docSeries == null ? null : new StringElement(docSeries);

        this.docNum = new StringElement(docNum);

        this.issueDate = new StringElement( XmlLoader.formatDate(issueDate) );

        this.docIssuer = docIssuer == null ? null : new StringElement(docIssuer);

        this.deptCode = deptCode == null ? null : new StringElement(deptCode);
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
}
