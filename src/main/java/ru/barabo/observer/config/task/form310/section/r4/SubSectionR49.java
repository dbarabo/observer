package ru.barabo.observer.config.task.form310.section.r4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р4.9")
public class SubSectionR49 {

    @XStreamAlias("Р4.9_2")
    final private String typeGoodsCirculation;

    public  SubSectionR49(String typeGoodsCirculation) {

        this.typeGoodsCirculation = typeGoodsCirculation;
    }
}
