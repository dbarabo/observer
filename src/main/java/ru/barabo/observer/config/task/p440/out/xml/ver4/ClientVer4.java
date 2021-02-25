package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerIp;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerPhysic;

@XStreamAlias("КлиентБанк")
public class ClientVer4 {

    @XStreamAlias("КлиентЮЛ")
    private PayerJur payerJur;

    @XStreamAlias("КлиентИП")
    private PayerIp payerIp;

    @XStreamAlias("КлиентФЛ")
    private PayerPhysic payerPhysic;
}
