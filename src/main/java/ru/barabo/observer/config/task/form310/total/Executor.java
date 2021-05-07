package ru.barabo.observer.config.task.form310.total;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.skad.forms.form310.ExecutorForm;
import ru.barabo.observer.config.skad.forms.form310.Form310Data;

@XStreamAlias("Исполнитель")
public class Executor {

    @XStreamAlias("Должность")
    final private String post;

    @XStreamAlias("ФИО")
    final private String fio;

    @XStreamAlias("Телефон")
    final private String phone;

    public Executor(ExecutorForm executor) {

        post = executor.getPost();

        fio = executor.getName();

        phone = executor.getPhone();
    }
}
