package ru.barabo.observer.config.task.nbki.gutdf.legal.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.nbki.gutdf.general.AbstractEventData;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul10DealUid;
import ru.barabo.observer.config.task.nbki.gutdf.legal.block.Ul30Court;

import java.util.Date;

/**
 * Изменились сведения о судебном споре или требовании по обязательству
 */
public class UlEvent2_6 extends AbstractEventData {

    @XStreamAlias("operationCode")
    final String operationCode = "B";

    @XStreamAlias("UL_10_DealUid")
    final Ul10DealUid ul10DealUid; // Блок 10. Идентификатор сделки

    @XStreamAlias("UL_30_Court")
    final Ul30Court ul30Court; // Блок 10. Идентификатор сделки

    public UlEvent2_6(Integer orderNum, Date eventDate, Ul10DealUid ul10DealUid, Ul30Court ul30Court) {

        super(orderNum, eventDate);

        this.ul10DealUid = ul10DealUid;

        this.ul30Court = ul30Court;
    }
}

