package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Блок 4. Номер налогоплательщика
 */
public class Ul4TaxType {

    @XStreamImplicit(itemFieldName = "TaxNum_group_UL_4_Tax")
    private final List<TaxNumGroupUl4Tax> taxNumGroupUl4TaxList; // Номер налогоплательщика

    public Ul4TaxType(List<TaxNumGroupUl4Tax> taxNumGroupUl4TaxList) {

        this.taxNumGroupUl4TaxList = taxNumGroupUl4TaxList;
    }

    public List<TaxNumGroupUl4Tax> getTaxNumGroupUl4TaxList() {
        return taxNumGroupUl4TaxList;
    }
}
