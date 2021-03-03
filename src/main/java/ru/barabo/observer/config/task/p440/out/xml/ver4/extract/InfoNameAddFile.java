package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ИмДопФ")
public class InfoNameAddFile {

    @XStreamAlias("ДопФайл")
    private String nameWithoutExt;

    public InfoNameAddFile(String nameWithoutExt) {

        this.nameWithoutExt = nameWithoutExt;
    }
}
