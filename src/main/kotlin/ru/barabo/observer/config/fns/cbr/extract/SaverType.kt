package ru.barabo.observer.config.fns.cbr.extract

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator
import ru.barabo.observer.config.barabo.p440.out.GeneralCreator.Companion.validateXml
import ru.barabo.observer.config.barabo.p440.out.ResponseData
import ru.barabo.observer.config.barabo.p440.out.data.ExtractMainResponseDataVer4
import ru.barabo.observer.config.fns.cbr.CbrConfig
import ru.barabo.observer.config.fns.cbr.task.getCbrResponseToday
import ru.barabo.observer.config.skad.plastic.task.saveXml
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.FileExtractMainXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.pb.FilePbXmlVer4
import ru.barabo.observer.config.task.template.db.DbSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.time.Duration
import java.time.LocalTime
import kotlin.reflect.KClass

enum class SaverType(val dbValue: Int, val outObject : ActionTask?) {

    NONE(0, null),
    PB(1, PbSaverCbr ),
    EXTRACT(2, ExtractMainCbr),
    EXTRACT_ADDITIONAL(3, null);

    companion object {
        private val HASH_DBVALUE_CREATOR = values().map { it -> Pair(it.dbValue, it.outObject) }.toMap()

        fun creatorByDbValue(value: Int) : ActionTask? = HASH_DBVALUE_CREATOR[value]
    }
}

object PbSaverCbr : AbstractCbrSaver<FilePbXmlVer4>(PbResponseDataCbr(), FilePbXmlVer4::class) {

    override fun name(): String = "ЦБ-Выписки выгрузка PB-файла"
}

object ExtractMainCbr : GeneralCreator<FileExtractMainXmlVer4>(ExtractMainResponseDataVer4(), FileExtractMainXmlVer4::class) {

    override fun name(): String = "Выгрузка выписки ver.4"
}

abstract class AbstractCbrSaver<X: Any>(val responseData: ResponseData, val clazzXml : KClass<X>) : DbSelector, ActionTask {
    override fun config(): ConfigTask = CbrConfig

    override val select: String = "select id, FILENAME, TYPE_RESPONSE from od.PTKB_CBR_EXT_RESPONSE where state = 0"

    override val accessibleData: AccessibleData = AccessibleData(workTimeFrom = LocalTime.of(8, 0),
        workTimeTo = LocalTime.of(20, 0), executeWait = Duration.ofSeconds(1))

    override fun actionTask(selectorValue: Any?): ActionTask {

        if(selectorValue !is Number)
            throw Exception("PTKB_CBR_EXT_RESPONSE.TYPE_RESPONSE is not valid value = $selectorValue  ${selectorValue?.javaClass}}"  )

        val creator = SaverType.creatorByDbValue( selectorValue.toInt() )

        return creator ?: throw Exception("PTKB_CBR_EXT_RESPONSE.TYPE_RESPONSE is not valid value = $selectorValue")
    }

    override fun actionTask(): ActionTask = this

    override fun execute(elem: Elem): State {

        val sessionSetting = AfinaQuery.uniqueSession()

        responseData.init(elem.idElem!!, sessionSetting)

        val xmlData = createXml(clazzXml, responseData)

        val file = File("${getCbrResponseToday().absolutePath}/${responseData.fileNameResponse()}.xml")

        saveXml(file, xmlData)

        validateXml(file, responseData.xsdSchema())

        AfinaQuery.execute(UPDATE_STATE_RESPONSE, arrayOf(elem.idElem), sessionSetting)

        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }
}

fun<X: Any, Y: Any> createXml(classXml : KClass<X>, param: Y): X {

    val parClass = param.javaClass.name.substringBefore('!')

    for(construct in classXml.constructors) {

        if(construct.parameters.size != 1) continue

        val paramClassName = construct.parameters.iterator().next().type.toString().substringBefore('!')

        if(parClass == paramClassName) {

            return construct.call(param)
        }
    }

    throw Exception("Constructor not found for class=${classXml} with param=$param")
}

private const val UPDATE_STATE_RESPONSE = "update od.PTKB_CBR_EXT_RESPONSE set state = 1, sent = sysdate where id = ?"