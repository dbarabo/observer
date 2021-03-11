package ru.barabo.observer.config.task.p440.out.xml.ver4.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.text.SimpleDateFormat;
import java.util.Date;

@XStreamAlias("ДОКНО")
public class PbDocument {

    @XStreamAlias("ИмяФайла")
    private String fileName;

    @XStreamAlias("ДатаВремяПроверки")
    final private String checkEnd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

    public PbDocument(String fileName) {

        this.fileName = fileName;
    }
}
