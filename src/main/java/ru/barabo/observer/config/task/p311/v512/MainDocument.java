package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@XStreamAlias("Документ")
public final class MainDocument {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static String dateFormat(Date date) {
        return DATE_FORMAT.format(date);
    }

    @XStreamAlias("ИдДок")
    private final String idDoc = UUID.randomUUID().toString().toUpperCase();

    @XStreamAlias("КНД")
    private String knd; // for Juric = "1114301", for Physic = "1114315"

    @XStreamAlias("КодНОБ")
    private String codeFns;

    @XStreamAlias("НомСооб")
    private String numberMessage;

    @XStreamAlias("ТипСооб")
    private String typeMessage;

    @XStreamAlias("ДолжнПрБ")
    private final String positionMainBank = "Начальник ОРОиОС ДОК";

    @XStreamAlias("ФамПрБ")
    private final String secondNameMainBank = "Кокшарова Е.В.";

    @XStreamAlias("ТелБанка")
    private final String phoneMainBank = "(423)220-96-93";

    @XStreamAlias("ДатаСооб")
    private final String dateMessage = dateFormat(new Date() );

    @XStreamAlias("СвБанк")
    private final SvBank svBank = new SvBank();

    @XStreamAlias("СвНП")
    private SvNp svNp;

    @XStreamAlias("СвСчет")
    private SvAccount svAccount;

    public MainDocument(Integer isPhysic, String codeFns, String numberMessage,
                        String typeMessage, SvNp svNp, SvAccount svAccount) {

        this.knd = (isPhysic == 0) ? "1114301" : "1114315";

        this.codeFns = codeFns;
        this.numberMessage = numberMessage;
        this.typeMessage = typeMessage;

        this.svNp = svNp;
        this.svAccount = svAccount;
    }
}
