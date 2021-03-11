package ru.barabo.observer.config.task.p440.out.xml.ver4.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.ParamsQuery;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerIp;
import ru.barabo.observer.config.task.p440.load.xml.impl.PayerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XStreamAlias("КлиентИП")
public class PayerIpVer4 implements ParamsQuery {

    @XStreamAlias("ИННИП")
    private String inn;

    @XStreamAlias("ФИОИП")
    private Fio fio;

    transient private Number idClient;

    static public PayerIpVer4 fromPayerIp(PayerIp payerIp) {
        return payerIp == null ? null
                : new PayerIpVer4(payerIp.getIdClient(), payerIp.getInn(), payerIp.getFio() );
    }

    public PayerIpVer4() {
    }

    public PayerIpVer4(Number idClient, String inn, Fio fio) {
        this.idClient = idClient;
        this.inn = inn;

        this.fio = new Fio(fio.getFirstName(), fio.getLastName(), fio.getPapaName());
    }

    public PayerIpVer4(Number idClient, String inn, String firstName, String lastName, String secondName) {

        this.idClient = idClient;
        this.inn = inn;

        fio = new Fio(firstName, lastName, secondName);
    }

    public String getInn() {
        return inn;
    }

    public Fio getFio() {
        return fio;
    }

    @Override
    public List<Object> getParams() {

        return new ArrayList<Object>(Arrays.asList(inn == null ? String.class : inn,
                fio == null || fio.getFirstName() == null ? String.class : fio.getFirstName(),
                fio == null || fio.getLastName() == null ? String.class : fio.getLastName(),
                fio == null || fio.getPapaName() == null ? String.class : fio.getPapaName(),
                PayerType.Pboul.getValueDb()));
    }

    private static final String COLUMNS = "INN, FIRST_NAME, LAST_NAME, SECOND_NAME, TYPE, FNS_FROM, ID";

    @Override
    public String getListColumns() {

        return COLUMNS;
    }
}
