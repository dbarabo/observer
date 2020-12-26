package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio;

import java.sql.Timestamp;
import java.util.Date;

@XStreamAlias("НПФЛ")
public final class NpFl {

    @XStreamAlias("ИННФЛ")
    private String inn;

    @XStreamAlias("ДатаРожд")
    private String birthDate;

    @XStreamAlias("МестоРожд")
    private String birthPlace;

    @XStreamAlias("КодДУЛ")
    private String codeDocuments;

    @XStreamAlias("СерНомДок")
    private String lineNumberDocument;

    @XStreamAlias("ДатаДок")
    private String dateOutDocument;

    @XStreamAlias("ФИОФЛ")
    private Fio fio;

    public NpFl(String inn, Date birthDate, String birthPlace, String codeDocuments, String lineNumberDocument,
                Date dateOutDocument, String firstName, String lastName, String secondName) {

        this.inn = inn;
        this.birthDate = MainDocument.dateFormat(birthDate);

        this.birthPlace = birthPlace == null ? "Неизвестно" : birthPlace;

        this.codeDocuments = codeDocuments == null ? "91" : codeDocuments;

        this.lineNumberDocument = lineNumberDocument;

        this.dateOutDocument = dateOutDocument == null ? "11.11.1111" : MainDocument.dateFormat(dateOutDocument);

        this.fio = new Fio(firstName, lastName, secondName);
    }
}
