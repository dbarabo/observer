package ru.barabo.p600.exchange;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.text.SimpleDateFormat;
import java.util.Date;

@XStreamAlias("СлужЧасть")
public class ServicePart {

    @XStreamAlias("ВерсФорм")
    private String versionFormat = "1.1";

    @XStreamAlias("ВерсПрог")
    private String versionProga = "JAFINA 5.5";

    @XStreamAlias("ДатВып")
    private String dateExtract = formatDate(new Date() );

    @XStreamAlias("ВремВып")
    private String timeExtract = formatTime(new Date() );

    @XStreamAlias("НачДат")
    private String startExtract;

    @XStreamAlias("КонечДат")
    private String endExtract;


    @XStreamAlias("ФИООтвСотрудн")
    private FioEmployer oper = new FioEmployer();

    @XStreamAlias("ТелОтвСотрудн")
    private String phone = "(423)2229882";

    public ServicePart(Date startExtract, Date endExtract) {
        this.startExtract = formatDate(startExtract);

        this.endExtract = formatDate(endExtract);
    }

    transient private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        return formatter.format(date);
    }

    transient private static final String TIME_FORMAT = "HH:mm:ss";

    public static String formatTime(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);

        return formatter.format(date);
    }

    public static String formatTimeShort(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(TIME_SHORT_FORMAT);

        return formatter.format(date);
    }

    transient private static final String TIME_SHORT_FORMAT = "HH:mm";
}
