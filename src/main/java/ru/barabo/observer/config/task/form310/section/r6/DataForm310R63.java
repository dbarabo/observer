package ru.barabo.observer.config.task.form310.section.r6;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Р6.3")
public class DataForm310R63 {

    @XStreamAlias("Р6.3_1")
    final private String priorIdCodeSubject;

    @XStreamAlias("Р6.3_2")
    final private String validIdCodeSubject;

    public DataForm310R63(Number priorIdCodeSubject, Number validIdCodeSubject) {

        this.priorIdCodeSubject = priorIdCodeSubject.toString();

        this.validIdCodeSubject = validIdCodeSubject.toString();
    }
}
