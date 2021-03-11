package ru.barabo.observer.config.task.p440.out.xml.ver4;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerJur;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerPhysic;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerXml;
import ru.barabo.observer.config.task.p440.out.xml.ver4.total.PayerIpVer4;

@XStreamAlias("КлиентБанк")
public class ClientVer4 {

    @XStreamAlias("КлиентЮЛ")
    private PayerJur payerJur;

    @XStreamAlias("КлиентИП")
    private PayerIpVer4 payerIp;

    @XStreamAlias("КлиентФЛ")
    private PayerPhysic payerPhysic;

    public static ClientVer4 fromPayerXml(PayerXml payerXml) {
        ClientVer4 client = new ClientVer4();

        client.payerJur = payerXml.getPayerJur();

        client.payerIp = PayerIpVer4.fromPayerIp(payerXml.getPayerIp());

        client.payerPhysic = payerXml.getPayerPhysic();

        return client;
    }
}
