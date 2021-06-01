package ru.barabo.observer.config.task.form310;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.skad.forms.form310.Data310Form;
import ru.barabo.observer.config.task.form310.section.r1.DataForm310R1;
import ru.barabo.observer.config.task.form310.section.r2.DataForm310R2;
import ru.barabo.observer.config.task.form310.section.r3.DataForm310R3;
import ru.barabo.observer.config.task.form310.section.r4.DataForm310R4;
import ru.barabo.observer.config.task.form310.section.r5.DataForm310R5;
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R61;
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R62;
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R63;
import ru.barabo.observer.config.task.form310.section.r6.DataForm310R64;

import java.util.List;

@XStreamAlias("Данные310")
public class DataForm310 {

    @XStreamAlias("Ид")
    final private Integer id = 1;

    @XStreamImplicit(itemFieldName = "Р1")
    final private List<DataForm310R1> dataForm310R1;

    @XStreamImplicit(itemFieldName = "Р2")
    final private List<DataForm310R2> dataForm310R2;

    @XStreamImplicit(itemFieldName = "Р3")
    final private List<DataForm310R3> dataForm310R3;

    @XStreamImplicit(itemFieldName = "Р4")
    final private List<DataForm310R4> dataForm310R4;

    @XStreamImplicit(itemFieldName = "Р5")
    final private List<DataForm310R5> dataForm310R5;

    @XStreamImplicit(itemFieldName = "Р6.1")
    final private List<DataForm310R61> dataForm310R61;

    @XStreamImplicit(itemFieldName = "Р6.2")
    final private List<DataForm310R62> dataForm310R62;

    @XStreamImplicit(itemFieldName = "Р6.3")
    final private List<DataForm310R63> dataForm310R63;

    @XStreamImplicit(itemFieldName = "Р6.4")
    final private List<DataForm310R64> dataForm310R64;


    public DataForm310(Data310Form data) {

        dataForm310R1 = data.getSectionsR1();

        dataForm310R2 = data.getSectionsR2().isEmpty() ? null : data.getSectionsR2();

        dataForm310R3 = data.getSectionsR3().isEmpty() ? null : data.getSectionsR3();

        dataForm310R4 = data.getSectionsR4().isEmpty() ? null : data.getSectionsR4();

        dataForm310R5 = data.getSectionsR5().isEmpty() ? null : data.getSectionsR5();

        dataForm310R61 = data.getSectionsR61().isEmpty() ? null : data.getSectionsR61();

        dataForm310R62 = data.getSectionsR62().isEmpty() ? null : data.getSectionsR62();

        dataForm310R63 = data.getSectionsR63().isEmpty() ? null : data.getSectionsR63();

        dataForm310R64 = data.getSectionsR64().isEmpty() ? null : data.getSectionsR64();
    }
}
