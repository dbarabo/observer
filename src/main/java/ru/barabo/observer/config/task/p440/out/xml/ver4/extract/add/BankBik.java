package ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("КодБП")
public class BankBik {

    @XStreamAlias("БИКБП")
    private String bikRussia;

    @XStreamAlias("KoflSWBIiH")
    private String swift;

    public BankBik(String bik) {

        if(bik.matches("\\d+") && bik.length() == 9 && "04".equals(bik.substring(0,2))) {
            bikRussia = bik;
        } else {
            swift = bik;
        }
    }
}
