package ru.barabo.observer.config.task.nbki.gutdf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.afina.AfinaQuery;
import ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.GutdfData;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("Document")
public class MainDocument {

    @XStreamAlias("schemaVersion")
    final private String schemaVersion = "4.1";

    @XStreamAlias("inn")
    final private String inn = "2540015598";

    @XStreamAlias("ogrn")
    final private String ogrn = "1022500001325";

    @XStreamAlias("sourceID")
    final private String sourceID;

    @XStreamAlias("regNumberDoc")
    final private String filenameWithoutExt;

    @XStreamAlias("dateDoc")
    final private String dateDoc;

    @XStreamAlias("subjectsCount")
    final private Integer subjectsCount;

    @XStreamAlias("groupBlocksCount")
    final private Integer groupBlocksCount;

    @XStreamAlias("Source")
    final private Source source;

    @XStreamAlias("Data")
    final private Data data;

    public MainDocument(GutdfData gutdfData) {

        sourceID = gutdfData.filenameWithoutExt().substring(0, "K301BB000001".length());

        filenameWithoutExt = gutdfData.filenameWithoutExt();

        dateDoc = XmlLoader.formatDate(gutdfData.dateDocument() );

        subjectsCount = gutdfData.subjectsCount();

        groupBlocksCount = gutdfData.groupBlocksCount();

        source = new Source(gutdfData);

        data = new Data(gutdfData);
    }

    public Data getData() {
        return data;
    }
}
