package ru.barabo.observer.config.task.p311.ticket;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("UV")
public class TicketCbr {

    @XStreamAlias("TU_OKATO")
    private String okato;

    @XStreamAlias("TU_NAME")
    private String name;

    @XStreamAlias("TU_DATE")
    private String date;

    @XStreamAlias("TU_TIME")
    private String time;

    @XStreamAlias("BIK")
    private String bik;

    @XStreamAlias("ARH")
    private String archiveName;

    @XStreamAlias("SIZE_ARH")
    private String sizeArch;

    @XStreamAlias("DATE_ARH")
    private String dateArch;

    @XStreamAlias("TIME_ARH")
    private String timeArch;

    @XStreamAlias("NAME")
    private NameRecords nameRecords;

    @XStreamAlias("REZ_ARH")
    private String resultArchive;

    @XStreamAlias("DATE_UV")
    private String dateTicket;

    @XStreamAlias("TIME_UV")
    private String timeTicket;

    @XStreamAlias("OPER")
    private String operator;

    @XStreamAlias("TEL_OPER")
    private String phone;

    public String getArchiveName() {
        return archiveName;
    }

    public NameRecords getNameRecords() {
        return nameRecords;
    }

    public String getResultArchive() {
        return resultArchive;
    }
}
