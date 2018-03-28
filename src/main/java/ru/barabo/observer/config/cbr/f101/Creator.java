package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Составитель")
public class Creator {

    @XStreamAlias("Руководитель")
    private Mainer mainer;

    @XStreamAlias("ГлавБух")
    private MainBuh mainBuh;

    @XStreamAlias("Исполнитель")
    private Executor executor;
}
