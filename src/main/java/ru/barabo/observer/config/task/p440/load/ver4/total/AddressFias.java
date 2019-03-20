package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("АдрПлат")
public class AddressFias {

    @XStreamAlias("ИдНом")
    private String uin;

    @XStreamAlias("Индекс")
    private String index;

    @XStreamAlias("Регион")
    private String codeRegion;

    @XStreamAlias("МуниципРайон")
    private AddressKindTypeName district;

    @XStreamAlias("ГородСелПоселен")
    private AddressKindTypeName town;

    @XStreamAlias("НаселенПункт")
    private Locality locality;

    @XStreamAlias("ЭлПланСтруктур")
    private ElementPlanStruct elementPlanStruct;

    @XStreamAlias("ЭлУлДорСети")
    private ElementPlanStruct elementStreetRoadNet;

    @XStreamAlias("ЗемелУчасток")
    private String landPlot;

    @XStreamAlias("Здание")
    private NumberType building;

    @XStreamAlias("ПомещЗдания")
    private NumberType block;

    @XStreamAlias("ПомещКвартиры")
    private NumberType flat;
}
