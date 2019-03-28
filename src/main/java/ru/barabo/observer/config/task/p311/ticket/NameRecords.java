package ru.barabo.observer.config.task.p311.ticket;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("NAME")
public class NameRecords {

    @XStreamAlias("nRec")
    private Integer countRecord;

    @XStreamImplicit(itemFieldName = "NAME_REC")
    private List<FileRecord> fileRecords;

    public List<FileRecord> getFileRecords() {
        return fileRecords;
    }
}
