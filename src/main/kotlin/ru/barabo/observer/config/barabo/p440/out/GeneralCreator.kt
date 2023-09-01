package ru.barabo.observer.config.barabo.p440.out

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import org.slf4j.LoggerFactory
import org.xml.sax.SAXException
import ru.barabo.cmd.XmlValidator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.data.AbstractResponseData
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.cbr.ticket.task.X440P
import ru.barabo.observer.config.fns.scad.CryptoScad
import ru.barabo.observer.config.skad.crypto.ScadConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.p440.load.xml.impl.*
import ru.barabo.observer.config.task.p440.out.xml.AbstractInfoPart
import ru.barabo.observer.config.task.p440.out.xml.AbstractToFns
import ru.barabo.observer.config.task.p440.out.xml.bnp.BnpInfoPart
import ru.barabo.observer.config.task.p440.out.xml.bnp.BnpXml
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistInfoPart
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistXml
import ru.barabo.observer.config.task.p440.out.xml.exists.ExistsAccount
import ru.barabo.observer.config.task.p440.out.xml.extract.*
import ru.barabo.observer.config.task.p440.out.xml.pb.PbInfoPart
import ru.barabo.observer.config.task.p440.out.xml.pb.PbResult
import ru.barabo.observer.config.task.p440.out.xml.pb.PbXml
import ru.barabo.observer.config.task.p440.out.xml.rest.RestAccount
import ru.barabo.observer.config.task.p440.out.xml.rest.RestInfoPart
import ru.barabo.observer.config.task.p440.out.xml.rest.RestXml
import ru.barabo.observer.config.task.p440.out.xml.ver4.*
import ru.barabo.observer.config.task.p440.out.xml.ver4.bnp.BnpInfoPartVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.bnp.FileBnpXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.ExistsAccountVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.ExistsInfoDetailVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.ExistsInfoPartVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.exists.FileExistsXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.*
import ru.barabo.observer.config.task.p440.out.xml.ver4.extract.add.*
import ru.barabo.observer.config.task.p440.out.xml.ver4.pb.FilePbXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.pb.PbDocument
import ru.barabo.observer.config.task.p440.out.xml.ver4.pb.PbInfoPartVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.rest.FileRestXmlVer4
import ru.barabo.observer.config.task.p440.out.xml.ver4.rest.RestInfoPartVer4
import ru.barabo.observer.config.task.template.db.DbSelector
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.Charset
import java.time.Duration
import java.time.LocalTime
import kotlin.reflect.KClass


fun String.byFolderExists(): File {
    val folder = File(this)

    if(!folder.exists()) {
        folder.mkdirs()
    }

    return folder
}

abstract class GeneralCreator<X: Any>(protected val responseData: AbstractResponseData, private val clazzXml : KClass<X>) : DbSelector, ActionTask {

    override fun config(): ConfigTask = CryptoScad //ScadConfig

    override val select: String = "select id, FILE_NAME, IS_PB from od.ptkb_440p_response where state = 0"

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.WORK_ONLY,
            false,
            LocalTime.of(9, 15),
            LocalTime.of(17, 45), Duration.ofSeconds(1))

    override fun actionTask(selectorValue: Any?): ActionTask {

        if(selectorValue !is Number) throw Exception("ptkb_440p_response.IS_PB is not valid value = $selectorValue  ${selectorValue?.javaClass}}"  )

        val creator = OutType.creatorByDbValue( selectorValue.toInt() )

        return creator ?: throw Exception("ptkb_440p_response.IS_PB is not valid value = $selectorValue")
    }

    override fun actionTask(): ActionTask = this

    private fun createXml(classXml :KClass<X>, responseData :AbstractResponseData) :X {
        val construct =  classXml.constructors.iterator().next()

        return construct.call(responseData)
    }

    override fun execute(elem: Elem): State {

        val sessionSetting = AfinaQuery.uniqueSession()

        responseData.init(elem.idElem!!, sessionSetting)

        val xmlData = createXml(clazzXml, responseData)

        val fileXml = saveXml(responseData.fileNameResponse(), xmlData,
            responseData.isPbFile() && responseData.isSourceSmev())

        validateXml(fileXml, responseData.xsdSchema())

        AfinaQuery.execute(UPDATE_STATE_RESPONSE, arrayOf(elem.idElem), sessionSetting)

        AfinaQuery.commitFree(sessionSetting)

        return State.OK
    }

    companion object {

        private const val STATE_SAVED = 2

        private const val UPDATE_STATE_RESPONSE = "update od.PTKB_440P_RESPONSE set state = $STATE_SAVED, updated = sysdate, sent = sysdate where id = ?"

        private val logger = LoggerFactory.getLogger(GeneralCreator::class.java)!!

        fun sendFolder440p(): File = "$X440P/${Get440pFiles.todayFolder()}/Отправка".byFolderExists()

        private fun failSendFolder440p() :File = "${sendFolder440p().absolutePath}/Fail".byFolderExists()

        private const val XML_HEADER = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n"

        fun saveXml(fileName: String, xmlData: Any, isPbSmevFile: Boolean = false): File {

            val folderSet = if(isPbSmevFile) sendFolder440pSmev().absolutePath else sendFolder440p().absolutePath

            val fileXml = File("$folderSet/$fileName")

            val outputStream = FileOutputStream(fileXml)

            val writer = OutputStreamWriter(outputStream, Charset.forName("windows-1251"))

            writer.write(XML_HEADER)

            getXStream().toXML(xmlData, writer)

            outputStream.close()

            return fileXml
        }

        @Throws(SAXException::class)
        fun validateXml(fileXml: File, xsdSchema: String) {

            try {
                XmlValidator.validate(fileXml, xsdSchema)

            } catch (e: Exception) {

                logger.error("validateXml $fileXml", e)

                if(fileXml.exists()) {
                    val failFile = File("${failSendFolder440p().absolutePath}/${fileXml.name}")
                    fileXml.copyTo(failFile, true)
                    fileXml.delete()
                }
                throw SAXException(e)
            }
        }

        private fun getXStream(): XStream {
            val xstream = XStream(DomDriver("windows-1251"))

            xstream.useAttributeFor(String::class.java)
            xstream.useAttributeFor(Int::class.java)
            xstream.useAttributeFor(Long::class.java)
            xstream.useAttributeFor(Double::class.java)
            xstream.useAttributeFor(BigDecimal::class.java)
            xstream.useAttributeFor(BigInteger::class.java)
            xstream.useAttributeFor(Boolean::class.java)
            xstream.useAttributeFor(Integer::class.java)

            xstream.processAnnotations(AbstractToFns::class.java)
            xstream.processAnnotations(PbInfoPart::class.java)
            xstream.processAnnotations(PbResult::class.java)
            xstream.processAnnotations(PbXml::class.java)

            xstream.processAnnotations(FioAttr::class.java)

            xstream.processAnnotations(AbstractInfoPart::class.java)
            xstream.processAnnotations(RestInfoPart::class.java)
            xstream.processAnnotations(RestXml::class.java)

            xstream.processAnnotations(RestAccount::class.java)
            xstream.processAnnotations(FnsXml::class.java)
            xstream.processAnnotations(BankXml::class.java)
            xstream.processAnnotations(PayerJur::class.java)
            xstream.processAnnotations(PayerIp::class.java)
            xstream.processAnnotations(PayerPhysic::class.java)

            xstream.processAnnotations(ExistsAccount::class.java)
            xstream.processAnnotations(ExistInfoPart::class.java)
            xstream.processAnnotations(ExistXml::class.java)

            xstream.processAnnotations(OperationAccount::class.java)
            xstream.processAnnotations(ExtractMainAccount::class.java)
            xstream.processAnnotations(AddExtractInfoPart::class.java)
            xstream.processAnnotations(AdditionalExtractXml::class.java)
            xstream.processAnnotations(AmountInfo::class.java)
            xstream.processAnnotations(BankInfo::class.java)
            xstream.processAnnotations(DocumentInfo::class.java)
            xstream.processAnnotations(ExtractMainInfoPart::class.java)
            xstream.processAnnotations(ExtractMainXml::class.java)
            xstream.processAnnotations(PayerInfo::class.java)

            xstream.processAnnotations(BnpInfoPart::class.java)
            xstream.processAnnotations(BnpXml::class.java)

            /// Version 4.0
            xstream.processAnnotations(AddExtractInfoPartVer4::class.java)
            xstream.processAnnotations(BankBik::class.java)
            xstream.processAnnotations(BankInfoVer4::class.java)
            xstream.processAnnotations(FileAddExtractXmlVer4::class.java)
            xstream.processAnnotations(OperationAccountVer4::class.java)
            xstream.processAnnotations(PayerInfoVer4::class.java)
            xstream.processAnnotations(ExtractMainAccountVer4::class.java)
            xstream.processAnnotations(ExtractMainInfoPartVer4::class.java)
            xstream.processAnnotations(FileExtractMainXmlVer4::class.java)
            xstream.processAnnotations(GroupInfoAddFile::class.java)
            xstream.processAnnotations(GroupTurnExtractAccount::class.java)
            xstream.processAnnotations(InfoExtractDetail::class.java)
            xstream.processAnnotations(InfoNameAddFile::class.java)
            xstream.processAnnotations(TurnExtractAccount::class.java)

            xstream.processAnnotations(FileRestXmlVer4::class.java)
            xstream.processAnnotations(RestInfoPartVer4::class.java)

            xstream.processAnnotations(ExistsInfoPartVer4::class.java)
            xstream.processAnnotations(FileExistsXmlVer4::class.java)

            xstream.processAnnotations(AbstractFileXmlVer4::class.java)
            xstream.processAnnotations(AccountAbsent::class.java)
            xstream.processAnnotations(AccountDatePeriod::class.java)
            xstream.processAnnotations(ClientVer4::class.java)
            xstream.processAnnotations(CurrencyAccount::class.java)
            xstream.processAnnotations(DatePeriodOut::class.java)
            xstream.processAnnotations(DateWorkStateOut::class.java)
            xstream.processAnnotations(GeneralInfoPartVer4::class.java)
            xstream.processAnnotations(InfoBankPBRVer4::class.java)
            xstream.processAnnotations(InfoDecisionDetail::class.java)
            xstream.processAnnotations(InfoRequestDetail::class.java)
            xstream.processAnnotations(InfoRequestVer4::class.java)
            xstream.processAnnotations(RestAccountVer4::class.java)
            xstream.processAnnotations(RestInfoDetailVer4::class.java)

            xstream.processAnnotations(ExistsAccountVer4::class.java)
            xstream.processAnnotations(ExistsInfoDetailVer4::class.java)

            xstream.processAnnotations(RestInfoRequestDecision::class.java)
            xstream.processAnnotations(RestVer4::class.java)
            xstream.processAnnotations(SvRestVer4::class.java)
            xstream.processAnnotations(TypeAccountVer4::class.java)

            xstream.processAnnotations(FilePbXmlVer4::class.java)
            xstream.processAnnotations(PbDocument::class.java)
            xstream.processAnnotations(PbInfoPartVer4::class.java)

            xstream.processAnnotations(FileBnpXmlVer4::class.java)
            xstream.processAnnotations(BnpInfoPartVer4::class.java)

            return xstream
        }
    }
}

fun sendFolder440pSmev(): File = "${GeneralCreator.sendFolder440p()}/smev".byFolderExists()