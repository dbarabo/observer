package ru.barabo.observer.config.task.p440.out.xml.ver4.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("СведДопФайл")
public class GroupInfoAddFile {

    @XStreamAlias("КолДопФ")
    private String countAddFiles;

    @XStreamImplicit(itemFieldName = "ИмДопФ")
    private List<InfoNameAddFile> infoNameAddFiles;

    private static class SingltonGroupInfoAddFile {
        private transient static final String ABSENT_NAME = "Отсутствует";

        private transient static final GroupInfoAddFile ABSENT_ADD_FILE = new GroupInfoAddFile(0, ABSENT_NAME);
    }

    static public GroupInfoAddFile absentAddFile() {
        return SingltonGroupInfoAddFile.ABSENT_ADD_FILE;
    }

    private GroupInfoAddFile(Integer countAddFiles, String addFileName) {

        this.countAddFiles = countAddFiles.toString();

        this.infoNameAddFiles = new ArrayList<InfoNameAddFile>();

        this.infoNameAddFiles.add( new InfoNameAddFile(addFileName) );
    }

    public GroupInfoAddFile(List<String> filenames) {

        this.countAddFiles = filenames.size() + "";

        this.infoNameAddFiles = new ArrayList<InfoNameAddFile>(filenames.size());

        for(int index = 0; index < filenames.size(); index++) {

            this.infoNameAddFiles.set(index, new InfoNameAddFile( filenames.get(index) ) );
        }
    }
}
