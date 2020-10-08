
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.cmd.XmlValidator
import ru.barabo.observer.config.task.finder.isFind
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import kotlin.concurrent.timer


class AnnotationTest {

    private val logger = LoggerFactory.getLogger(AnnotationTest::class.java)

    // @Test
    fun testAddArchiveDenied() {

        val processFile = File("C:/Temp/acquir-group-time.xls")

        val archiveFullPath = "C:/Temp/AFN_MIFNS00_0507717_20201007_00011.arj"

//        val stream = File(archiveFullPath).inputStream()
//        val bytes = ByteArray(16)
//        stream.read(bytes)

        Archive.addToArj(archiveFullPath, arrayOf(processFile))
        //val err = "Error (5): Permission denied"

    }

    //@Test
    fun testUpdateAll() {
        val dir = File("U:/Observer/0")

        dir.listFiles { f ->
            !f.isDirectory
        }.forEach {
            val text = it.readText(Charset.forName("windows-1251"))

            val newText = text.replace("2020-10-06T", "2020-10-07T")

            it.writeText(newText, Charset.forName("windows-1251") )
        }
    }


    //@Test
    fun testXmlValidate() {
        validate(File("loan2680420191225163729.xml"), File("loan-info.xsd"))
    }

    fun validate(xmlFile: File, xsd: File/*xsdInJar: String?*/) {

        val xmlStream = StreamSource(xmlFile)

        val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)

        //val urlXsd = XmlValidator::class.java.javaClass.getResource(xsdInJar)

        val schema = schemaFactory.newSchema(xsd)

        val validator = schema!!.newValidator()

        validator.validate(xmlStream)
    }

    //@Test
    fun testThreadLong() {
       var threadLong: Long = 0L

       timer(name = "thread 1", initialDelay = 1000, daemon = false, period = 500) {
            while(true) {
                logger.error("GET1 threadLong=$threadLong")

              /*  threadLong = System.currentTimeMillis()

                logger.error("SET1 threadLong=$threadLong")
*/
                Thread.sleep(1000)
            }
        }

        timer(name = "THREAD 2", initialDelay = 1000, daemon = false, period = 500) {
            while(true) {
                logger.error("GET2 threadLong")

                /*      logger.error("GET2 threadLong=$threadLong")

                threadLong = System.currentTimeMillis()

                   logger.error("SET2 threadLong=$threadLong")
   */
                Thread.sleep(1000)
            }
        }

        timer(name = "thread 3", initialDelay = 1000, daemon = false, period = 500) {
            while(true) {
                logger.error("GET3 threadLong")
              /*  logger.error("GET3 threadLong=$threadLong")

                threadLong = System.currentTimeMillis()

                logger.error("SET3 threadLong=$threadLong")
*/
                Thread.sleep(1000)
            }
        }

        timer(name = "THREAD 4", initialDelay = 1000, daemon = false, period = 500) {
            while(true) {
                logger.error("GET4 threadLong")
            /*    logger.error("GET4 threadLong=$threadLong")

                threadLong = System.currentTimeMillis()

                logger.error("SET4 threadLong=$threadLong")
*/
                Thread.sleep(1000)
            }
        }
    }

    //@Test
    fun testRegExp2() {
        val pattern = Pattern.compile(".*\\.(csv|xls|xlsx|doc|docx)", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val isFindDocx :Boolean = pattern.matcher("ZLB_0226_20190218.docx")?.matches()?:false

        val isFindDoc :Boolean = pattern.matcher("ZLB_0226_20190218.doc")?.matches()?:false

        val isFindXlsx :Boolean = pattern.matcher("ZLB_0226_20190218.xlsx")?.matches()?:false

        val isFindXls :Boolean = pattern.matcher("ZLB_0226_20190218.xls")?.matches()?:false

        logger.error("isFindDocx=$isFindDocx")
        logger.error("isFindDoc=$isFindDoc")
        logger.error("isFindXlsx=$isFindXlsx")
        logger.error("isFindXls=$isFindXls")
    }



    //@Test
    fun testRegExp() {

        val str1 = "KWTFCB_BNP1_PNO10507717_251020180109_000072_20180110_0021.XML"

        //str1.substringBeforeLast()

        val str2 = "IZVTUB_AFN_0507717_MIFNS00_20180112_00001.xml"

        val str3 = "APN10507717_254320180111_000316.xml"

        val str4 = "KWTTUB_10507717_254320180111_000316.xml"

        val pattern = Pattern.compile("(KWTFCB|IZVTUB).*\\.xml", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        logger.error("str1= ${pattern.matcher(str1).find()}")

        logger.error("str2= ${pattern.matcher(str2).find()}")

        logger.error("str3= ${pattern.matcher(str3).find()}")

        logger.error("str4= ${pattern.matcher(str4).find()}")
    }
}