package ru.barabo.observer.config.task.nbki.gutdf.legal.title;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Блок 3. Регистрационный номер
 */
public class Ul3Reg {

    @XStreamAlias("UL_3_Reg_current")
    private final Ul3RegType ul3RegCurrent;

    @XStreamAlias("UL_3_Reg_new")
    private final Ul3RegType ul3RegNew;

    public Ul3Reg(Ul3RegType ul3RegCurrent, Ul3RegType ul3RegNew) {

        this.ul3RegCurrent = ul3RegCurrent;
        this.ul3RegNew = ul3RegNew;
    }
}
