package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

@XStreamAlias("СведОбОпер")
public class InfoOper {

    @XStreamAlias("НомРеестр")
    private String registerNumber = "";

    @XStreamAlias("НомОпер")
    private String operNumber = "";

    @XStreamAlias("ДатаОпер")
    private String dateOper = "";

    @XStreamAlias("ВремОпер")
    private String timeOper = "";

    @XStreamAlias("КодВидОпер")
    private String codeKindOper = "";

    @XStreamAlias("КодВалПрин")
    private String currencyReceived = "";

    @XStreamAlias("КурсВал")
    private String exchangeCurrency = "";

    @XStreamAlias("СумПрин")
    private String amountReceived = "";

    @XStreamAlias("КодВалВыд")
    private String currencyOut = "";

    @XStreamAlias("СумВыд")
    private String amountOut = "";

    @XStreamAlias("ПризИспКарта")
    private String isCard = "";

    @XStreamAlias("КолЧек")
    private String countCheck = "";

    @XStreamAlias("КодВалЧек")
    private String codeCurCheck = "";

    @XStreamAlias("СумЧек")
    private String amountCheck = "";

    @XStreamAlias("НомСчет")
    private String codeAccount = "";

    @XStreamAlias("ПризОпДовер")
    private String isProxy = "";

    @XStreamAlias("ГраждФЛ")
    private String citizenship = "";

    public InfoOper(Object[] row) {

        registerNumber = row[0] + "";

        operNumber = row[1] + "";

        dateOper = ServicePart.formatDate((Date)row[2]);

        timeOper = ServicePart.formatTimeShort((Date)row[2]);

        codeKindOper = row[3] + "";

        currencyReceived = row[4] + "";

        exchangeCurrency = formatCourse((Number)row[5]);

        amountReceived = XmlLoader.formatSum((Number)row[6]);

        currencyOut = row[7] + "";

        amountOut = XmlLoader.formatSum((Number)row[8]);

        isCard = row[9] + "";

        isProxy = row[10] + "";

        citizenship = row[11] + "";
    }

    public static String formatCourse(Number course) {
        if (course == null) {
            return null;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        return decimalFormat.format(course);
    }
}
