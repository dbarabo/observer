package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ФИООтвСотрудн")
public class FioEmployer {

    @XStreamAlias("Фам")
    private String fam = "Колтовская";

    @XStreamAlias("Имя")
    private String name = "Анна";

    @XStreamAlias("Отч")
    private String second = "Викторовна";
}
