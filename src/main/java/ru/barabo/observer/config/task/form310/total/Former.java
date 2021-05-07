package ru.barabo.observer.config.task.form310.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.skad.forms.form310.Form310Data;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Составитель")
public class Former {

    @XStreamAlias("ВидОрг")
    final private String kindOrganization = "КО";

    @XStreamAlias("ВидОрг")
    final private String codeOrganization = "21";

    @XStreamAlias("ВидОрг")
    final private String codeTU = "05";

    @XStreamAlias("БИК")
    final private String bik = "040507717";

    @XStreamAlias("ОКАТО")
    final private String okato = "09241";

    @XStreamAlias("ОКПО")
    final private String okpo = "09241840";

    @XStreamAlias("ОГРН")
    final private String ogrn = "1022500001325";

    @XStreamAlias("СокрНаимен")
    final private String shortName = "ООО \"ПРИМТЕРКОМБАНК\"";

    @XStreamAlias("Адрес")
    final private String address = "Г ВЛАДИВОСТОК УЛ.Семеновская,6Г";

    @XStreamAlias("ДатаПодписания")
    final private String dateSign = XmlLoader.formatDate( new Date() );

    @XStreamAlias("Руководитель")
    final private Executor leader;

    @XStreamAlias("ГлавБух")
    final private Executor chiefAccountant;

    @XStreamAlias("Исполнитель")
    final private Executor executor;

    public Former(Form310Data form310Data) {

        leader = new Executor(form310Data.getLeader());

        chiefAccountant = new Executor(form310Data.getChiefAccountant());

        executor = new Executor(form310Data.getExecutor());
    }
}
