package ru.barabo.observer.config.task.p311.ticket;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("NAME_REC")
public class FileRecord {

    @XStreamAlias("RecId")
    private Integer id;

    @XStreamAlias("NAME_ES")
    private String fileName;

    public String getFileName() {
        return fileName;
    }
}
