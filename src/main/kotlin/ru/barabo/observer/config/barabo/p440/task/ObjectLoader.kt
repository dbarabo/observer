package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.task.p440.load.xml.apx.ApnFromFns
import ru.barabo.observer.config.task.p440.load.xml.apx.ApoFromFns
import ru.barabo.observer.config.task.p440.load.xml.apx.ApzFromFns
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns
import ru.barabo.observer.config.task.p440.load.xml.decision.RpoFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsnFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsoFromFns
import ru.barabo.observer.config.task.p440.load.xml.request.ZsvFromFns

object RpoLoader : GeneralLoader<RpoFromFns>() {

    override fun name(): String = "Загрузка RPO-файла (арест)"
}

object RooLoader : GeneralLoader<RooFromFns>() {

    override fun name(): String = "Загрузка ROO-файла (отмена)"
}

object ZsnLoader : GeneralLoader<ZsnFromFns>() {

    override fun name(): String = "Загрузка ZSN-файла (наличие сч.)"
}

object ZsoLoader : GeneralLoader<ZsoFromFns>() {

    override fun name(): String = "Загрузка ZSO-файла (остатки)"
}

object ZsvLoader : GeneralLoader<ZsvFromFns>() {

    override fun name(): String = "Загрузка ZSV-файла (выписка)"
}

object ApnLoader : GeneralLoader<ApnFromFns>() {

    override fun name(): String = "Загрузка APN-файла (приост. инкасс)"
}

object ApoLoader : GeneralLoader<ApoFromFns>() {

    override fun name(): String = "Загрузка APO-файла (отм. приост. инкасс)"
}

object ApzLoader : GeneralLoader<ApzFromFns>() {

    override fun name(): String = "Загрузка APZ-файла (отзыв. инкасс)"
}



