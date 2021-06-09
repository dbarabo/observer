package ru.barabo.observer.config.task.form310.section.r1;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("РР1")
public class SubSectionR1123 {

    @XStreamImplicit(itemFieldName = "Р1с")
    final private List<SubSectionR1c> subSectionR1cList;

    @XStreamAlias("Р1.1")
    final private SubSectionR11 subSectionR11;

    @XStreamImplicit(itemFieldName = "Р1.2")
    final private List<SubSectionR12> subSectionR12;

    @XStreamImplicit(itemFieldName = "Р1.3")
    final private List<SubSectionR13> subSectionR13;

    public SubSectionR1123(List<String> r1cIdPactLoanList,
                           Date pledgePactOpen, String labelPledgePact,
                           List<String> idPledgerList,
                           List<String> idObjectPledgeList) {

        subSectionR1cList = new ArrayList();
        for (String idPact : r1cIdPactLoanList) {

            subSectionR1cList.add( new SubSectionR1c(idPact) );
        }

        subSectionR11 = new SubSectionR11(pledgePactOpen, labelPledgePact);

        subSectionR12 = new ArrayList();
        for (String idPledger : idPledgerList) {

            subSectionR12.add( new SubSectionR12(idPledger) );
        }

        subSectionR13 = new ArrayList();
        for (String idObject : idObjectPledgeList) {

            subSectionR13.add( new SubSectionR13(idObject) );
        }
    }
}
