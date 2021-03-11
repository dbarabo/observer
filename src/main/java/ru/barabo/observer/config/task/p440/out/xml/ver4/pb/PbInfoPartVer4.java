package ru.barabo.observer.config.task.p440.out.xml.ver4.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.out.xml.pb.PbResult;

import java.util.List;

@XStreamAlias("ПОДБНПРИНТ")
public class PbInfoPartVer4 {

    @XStreamAlias("ДОКНО")
    PbDocument pbDoc;

    @XStreamImplicit(itemFieldName = "Результат")
    private List<PbResult> pbResultList;

    public PbInfoPartVer4(String fileName, List<PbResult> pbResultList) {

        this.pbDoc = new PbDocument(fileName);

        this.pbResultList = pbResultList;
    }
}
