package ru.barabo.observer.config.barabo.p440.task

import ru.barabo.db.SessionSetting
import ru.barabo.observer.config.barabo.p440.GeneralLoader
import ru.barabo.observer.config.barabo.p440.saveData
import ru.barabo.observer.config.task.p440.load.xml.pno.OrderTax
import ru.barabo.observer.config.task.p440.load.xml.pno.PnoFromFns

object PnoLoader : GeneralLoader<PnoFromFns>() {

    override fun name(): String = "Загрузка PNO-файла (инкасс)"

    override fun saveOtherData(data: PnoFromFns, idFromFns: Number, idPayer: Number, sessionSetting: SessionSetting) {

        val pnoData =  (data.fromFnsInfo as OrderTax).pnoData

        pnoData.saveData(idFromFns, sessionSetting, ::insertPno)
    }
}

private fun insertPno(columns :String, questions :String) =
        "insert into od.PTKB_440P_PNO ($columns) values ($questions)"