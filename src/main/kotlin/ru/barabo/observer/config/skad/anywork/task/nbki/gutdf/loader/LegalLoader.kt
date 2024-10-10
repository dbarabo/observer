package ru.barabo.observer.config.skad.anywork.task.nbki.gutdf.loader

import ru.barabo.observer.config.task.nbki.gutdf.legal.SubjectUl
import java.util.ArrayList

fun processUl(idFile: Number, subjectUlList: List<SubjectUl>): List<DataInfo> {

    val data = ArrayList<DataInfo>()

    for(ul in subjectUlList) {

        //val taxPassport = ul.title.unical

        var idMain: Number = 0

//        l.events.flEvent1_1List?.forEach {
//            idMain = findMainId(idFile, taxPassport, it.event, it.unicalId, it.eventDate)
//            data.addAll(fl55Application(idMain, it.fl55Application))
       }

    return data
}