package ru.barabo.observer.config.task.form310;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ru.barabo.observer.config.skad.forms.form310.Form310Data;
import ru.barabo.observer.config.task.form310.total.Former;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;
import java.util.UUID;

@XStreamAlias("Ф0409310")
public class Form310Xml {
    @XStreamAsAttribute
    @XStreamAlias("xmlns")
    final String xmlns = "urn:cbr-ru:rep0409310:v0.2.4.5";

    @XStreamAlias("УникИдОЭС")
    final private String guid = UUID.randomUUID().toString().toUpperCase();

    @XStreamAlias("ВидОЭС")
    final private String kindOES =  "Отчетность КО";

    @XStreamAlias("КодФормы")
    final private String formCode =  "0409310";

    @XStreamAlias("ОКУД")
    final private String okud =  "0409310";

    @XStreamAlias("ВидОтчета")
    final private String kindReport =  "КО";

    @XStreamAlias("ОтчДата")
    final private String dateReport;

    @XStreamAlias("Периодичность")
    final private String periodicity = "месячная";

    @XStreamAlias("ДатаВремяФормирования")
    final private String dateTimeCreated = XmlLoader.formatDate( new Date() );

    @XStreamAlias("Составитель")
    final private Former former;

    @XStreamAlias("Данные310")
    final private DataForm310 dataForm310;

    @XStreamAlias("НетДанных")
    final private AbsentData310 absentData310;

    public Form310Xml(Form310Data form310Data) {

        dateReport =  XmlLoader.formatDate( form310Data.getDateReport() );

        former = new Former(form310Data);

        dataForm310 = (form310Data.getData() == null) ? null : new DataForm310(form310Data.getData());

        absentData310 = (form310Data.getData() == null) ? new AbsentData310() : null;
    }
}
