package ru.barabo.observer.config.skad.crypto.p311

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.XmlValidator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.ifTest
import ru.barabo.observer.config.cbr.ticket.task.Get440pFiles
import ru.barabo.observer.config.task.p311.v512.*
import ru.barabo.observer.config.task.p440.load.xml.impl.Fio
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.Charset
import java.sql.Date

private val logger = LoggerFactory.getLogger(MessageCreator311p::class.java)

object MessageCreator311p {

    fun createMessage(idMessage: Number): File {

        val mainFileData =  getMainFileData(idMessage)

        return saveXmlFile(idMessage, mainFileData)
    }

    private fun saveXmlFile(idMessage: Number, mainFileData: MainFile): File {

        val fileName = AfinaQuery.selectValue(SELECT_FILENAME, arrayOf(idMessage) ) as String

        val xmlFile = saveXml(fileName, mainFileData, "windows-1251")

        val xsd = if(fileName.indexOf("SF") == 0) "/xsd/SFC0_512.xsd" else "/xsd/SBC0_512.xsd"

        validateXml(xmlFile, xsd, ::errorFolder )

        return xmlFile
    }

    private fun saveXml(fileName: String, xmlData: Any, charsetName: String): File {

        val file = fullFile(fileName)

        FileOutputStream(file).use { out ->
            OutputStreamWriter(out, Charset.forName(charsetName)).use { writer ->
                writer.write(XML_HEADER)

                getXStream(charsetName).toXML(xmlData, writer)
            }
        }

        return file
    }

    private fun fullFile(fileName :String): File {

        val folder = if (fileName.isPhysic() ) physicFolder() else juricFolder()

        return File("${folder.absolutePath}/$fileName")
    }

    private fun getMainFileData(idMessage: Number): MainFile {
        val sessionSetting = AfinaQuery.uniqueSession()

        val mainFile = try {

            val info = AfinaQuery.execute(EXEC_GET_MESSAGE_INFO, arrayOf(idMessage), sessionSetting,
                intArrayOf(OracleTypes.VARCHAR,
                    OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
                    OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.DATE, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.DATE,
                    OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
                    OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
                    OracleTypes.DATE, OracleTypes.VARCHAR, OracleTypes.DATE
                ))

            val idFile = info?.get(0) as String

            val mainDocument = createMainDocument(info.drop(1))

            MainFile(idFile, mainDocument)
        } catch (e: Exception) {

            logger.error("createMessage idMessage=$idMessage", e)

            AfinaQuery.rollbackFree(sessionSetting)

            throw Exception(e)
        }

        AfinaQuery.commitFree(sessionSetting)

        return mainFile
    }

    private fun createMainDocument(info: List<Any?>): MainDocument {

        val isPhysic = (info[0] as Number).toInt()

        val codeFns = info[1] as String

        val numberMessage = info[2] as String

        val typeMessage = info[3] as String

        val infoAccount = info.drop(4)

        val svAccount = createSvAccount(isPhysic != 0, infoAccount)

        val infoNp = infoAccount.drop(6)

        val svNp = createSvNp(infoNp)

        return MainDocument(isPhysic, codeFns, numberMessage, typeMessage, svNp, svAccount)
    }

    private fun createSvNp(info: List<Any?>): SvNp {

        val codeFace = (info[0] as Number).toInt()

        val svidNU = info[1] as? String

        val inn = info[2] as? String

        val kpp = info[3] as? String
        val lineNumberDocument = kpp

        val ogrn = info[4] as? String
        val birthPlace = ogrn

        val mainName = info[5] as String

        val firstName = info[6] as? String
        val kio = firstName

        val secondName = info[7] as? String

        val birthDate = info[8] as? Date

        val codeDocuments = info[9] as? String

        val dateOutDocument = info[10] as? Date

        return when(TypeFace.typeFaceByCode(codeFace)) {
            TypeFace.OOO -> SvNp(codeFace, svidNU, NpRo(inn!!, kpp!!, ogrn!!, mainName) )

            TypeFace.IP -> SvNp(codeFace, svidNU, NpIP(inn!!, ogrn!!, firstName!!, mainName, secondName) )

            TypeFace.ForeignOOO -> SvNp(codeFace, svidNU, NpIO(mainName, inn, kio) )

            TypeFace.Physic -> SvNp(codeFace, NpFl(inn, birthDate, birthPlace, codeDocuments, lineNumberDocument,
                dateOutDocument, firstName!!, mainName, secondName))
        }
    }

    private fun createSvAccount(isPhysic: Boolean, info: List<Any?>): SvAccount {

        val isOpened = ((info[0] as Number).toInt() != 0)

        val code = info[1] as String

        val currency = code.substring(5..7).toInt().let { if(it == 810) 643 else it }

        val dateOpen = info[2] as Date

        val typeAccount = info[3] as String

        val numberPact = info[4] as? String

        val dateOpenClosePact = info[5] as Date

        val isPhysicNumber = if (isPhysic) 1 else null

        return SvAccount(code, dateOpen, typeAccount, currency, isPhysicNumber, isOpened, numberPact, dateOpenClosePact)
    }

    private const val SELECT_FILENAME = "select od.PTKB_FNS_EXPORT_XML.getFileName(?) from dual"

    private const val EXEC_GET_MESSAGE_INFO = """{ call od.PTKB_FNS_EXPORT_XML.getMessageInfo(
        ?, /*idFile*/
        ?, ?, ?, ?, /*isPhysic, codeFns, numberMessage, typeMessage*/
        ?, ?, ?, ?, ?, ?, /*isOpened, code, dateOpen, typeAccount, numberPact, dateOpenPact/dateCloseAccount*/
        ?, ?, ?, ?, ?, ?, ?, ?,  /*codeFace, svidNU, inn, kpp/lineNumberDocument, ogrn/birthPlace, Last/Name, firstName/KIO, secondName*/
           ?, ?, ?, /*birthDate, codeDocuments, dateOutDocument*/
        ?) }"""
}

private const val XML_HEADER = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n"

private fun String.isPhysic() = indexOf("SF") == 0

private val X311P = "X:/311-П".ifTest("C:/311-П")

private fun errorFolder(): File = Cmd.createFolder("$X311P/ФИЗИКИ/Отправка/${Get440pFiles.todayFolder()}/ERROR")

private fun physicFolder(): File = Cmd.createFolder("$X311P/ФИЗИКИ/Отправка/${Get440pFiles.todayFolder()}")

private fun juricFolder(): File = Cmd.createFolder("$X311P/Отправка/${Get440pFiles.todayFolder()}")

private fun getXStream(charsetName: String): XStream {
    val xstream = XStream(DomDriver(charsetName))

    xstream.useAttributeFor(String::class.java)
    xstream.useAttributeFor(Int::class.java)
    xstream.useAttributeFor(Long::class.java)
    xstream.useAttributeFor(Double::class.java)
    xstream.useAttributeFor(BigDecimal::class.java)
    xstream.useAttributeFor(BigInteger::class.java)
    xstream.useAttributeFor(Boolean::class.java)
    xstream.useAttributeFor(Integer::class.java)

    xstream.processAnnotations(MainFile::class.java)
    xstream.processAnnotations(MainDocument::class.java)
    xstream.processAnnotations(SvAccount::class.java)
    xstream.processAnnotations(AccountOpen::class.java)
    xstream.processAnnotations(AccountClose::class.java)

    xstream.processAnnotations(SvBank::class.java)
    xstream.processAnnotations(Fio::class.java)

    xstream.processAnnotations(SvNp::class.java)
    xstream.processAnnotations(NpRo::class.java)
    xstream.processAnnotations(NpIP::class.java)
    xstream.processAnnotations(NpIO::class.java)
    xstream.processAnnotations(NoInn::class.java)
    xstream.processAnnotations(NpFl::class.java)

    return xstream
}

enum class TypeFace(private val code: Int) {
    OOO(1),
    IP(2),
    ForeignOOO(6),
    Physic(7);

    companion object {
        fun typeFaceByCode(code: Int): TypeFace =
            values().firstOrNull { it.code == code } ?: throw Exception("TypeFace not found for code = $code")
    }
}

fun validateXml(file: File, xsdSchema: String, folderIfError: ()->File) {

    try {
        XmlValidator.validate(file, xsdSchema)

    } catch (e: Exception) {
        logger.error("validateXml $file", e)

        if(file.exists()) {
            val errorFile = File("${folderIfError().absolutePath}/${file.name}")

            file.copyTo(errorFile, true)

            file.delete()
        }

        throw Exception(e)
    }
}