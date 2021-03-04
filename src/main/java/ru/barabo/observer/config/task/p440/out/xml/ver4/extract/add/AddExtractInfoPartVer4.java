package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.AddExtractResponseDataVer4;

import java.util.List;

@XStreamAlias("ВЫПБНДОПОЛ")
public class AddExtractInfoPartVer4 {

    @XStreamAlias("ПорНом")
    private String orderAccountFile;

    @XStreamAlias("ПорНомДФ")
    private String orderNumberFile;

    @XStreamAlias("НомСч")
    private String account;

    @XStreamImplicit(itemFieldName = "ОперацииСЧ")
    private List<OperationAccountVer4> operationAccountVer4;

    public AddExtractInfoPartVer4(AddExtractResponseDataVer4 addExtractResponseData) {

        this.orderAccountFile = addExtractResponseData.getAccount().getOrderFile();

        this.orderNumberFile = String.format("%06d", addExtractResponseData.getOrderNumberFile());

        this.account = addExtractResponseData.getAccount().getCode();

        operationAccountVer4 = addExtractResponseData.getOperations();
    }
}
