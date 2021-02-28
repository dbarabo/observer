package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ВидСч")
public class TypeAccountVer4 {

    @XStreamAlias("КодВидСч")
    private String code;

    @XStreamAlias("ВидИнСч")
    private String name;

    public TypeAccountVer4(String code, String name) {
        this.code = code;

        if(code.equals("9999")) {
            this.name = name;
        }
    }
}
