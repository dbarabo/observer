package ru.barabo.observer.config.task.form310;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.skad.forms.form310.Data310Form;
import ru.barabo.observer.config.task.form310.section.r1.DataForm310R1;
import ru.barabo.observer.config.task.form310.section.r2.DataForm310R2;

import java.util.List;

@XStreamAlias("Данные310")
public class DataForm310 {

    @XStreamAlias("Ид")
    final private Integer id = 1;

    @XStreamImplicit(itemFieldName = "Р1")
    private List<DataForm310R1> dataForm310R1;

    @XStreamImplicit(itemFieldName = "Р2")
    private List<DataForm310R2> dataForm310R2;


    public DataForm310(Data310Form data) {

        dataForm310R1 = data.getSectionsR1();

        dataForm310R2 = data.getSectionsR2().isEmpty() ? null : data.getSectionsR2();
    }
}
