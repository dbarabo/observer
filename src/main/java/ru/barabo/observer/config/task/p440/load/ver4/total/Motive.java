package ru.barabo.observer.config.task.p440.load.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("МотивЗапр")
public class Motive {

    @XStreamAlias("КодОснов")
    private String codeReason;

    @XStreamAlias("Обоснов")
    private Justification just;
}
