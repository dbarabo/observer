
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.config.barabo.plastic.turn.loader.loadClearIntCp1251
import ru.barabo.observer.config.skad.crypto.task.AddSign600P
import ru.barabo.observer.config.skad.forms.form310.impl.DefaultForm310Data
import java.io.File
import java.nio.charset.Charset
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.regex.Pattern
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import kotlin.concurrent.timer


class AnnotationTest {

    private val logger = LoggerFactory.getLogger(AnnotationTest::class.java)

    //@Test
    fun testAddSign600P() {
        logger.error("NEXT=${AddSign600P.arjArchiveNameToday()}")
    }

    //@Test
    fun testUUID() {
        var uuid: UUID = UUID.randomUUID()
        var variant: Int = uuid.variant()
        var version: Int = uuid.version()
    }


    //@Test
    fun testClearIntLoaderImpl() {

        //val data = loader.load(File("C:/Temp/1/CLEARINT_20201204_053822_0226_3020.html"), Charset.forName ("cp1251"))

        //val data = loader.load(File("C:/Temp/1/CLEARINT_20200506_080813_0226_2872.html"), Charset.forName ("cp1251"))

        val data = loadClearIntCp1251(File("C:/Temp/1/CLEARINT_20200804_073441_0226_2933.html"))

        logger.error("$data")

    }

    //@Test
    fun test4regEx() {
        val formatDateTimeSlash = "dd/MM/yyyy HH:mm:ss"

        val line = "22/08/2020 00:00:00"

        val regexDateDot = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})")

       logger.error("${Timestamp(SimpleDateFormat(formatDateTimeSlash).parse(line).time)}")

       // logger.error("${regexDateDot.matcher( line ).takeIf { it.find() }?.group(1)}")
    }

    //@Test
    fun test3regEx() {
        val line = "Reason Code=7355 Description:11.23.2020"

        val regexDateDot = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})")

        //logger.error("${regexDateDot.matcher(line).find()}")

        logger.error("${regexDateDot.matcher( line ).takeIf { it.find() }?.group(1)}")
    }



    //@Test
    fun test2regEx() {
        val line = "<tr class=\"tr\"><td>02.12.2020</td><td>D</td><td align=\"right\">5815.62</td><td>810</td><td align=\"left\">Interchange ACQ 01.12.2020</td></tr>"

        val regexTableRow = Pattern.compile("<tr class=\"tr\">(.*?)</tr>")

        logger.error(regexTableRow.matcher( line ).takeIf { it.find() }?.group(1))
    }

    // @Test
    fun testRegexp() {

        val regexTableCell =  Pattern.compile("(<td[^>]*>(.*?)</td>)")

        val line = "<td>01.12.2020</td><td>C</td><td>810</td><td align=\"right\">1095.20</td><td align=\"left\">ACQ ATM REIMBURSEMENT FEES (FINANCAL, ATM NATL, 44 cnt)</td>"

        val matcher = regexTableCell.matcher(line)

        while(matcher.find()) {
            //logger.error("group1=${matcher.group(1)}")

            logger.error("group${matcher.groupCount()}=${matcher.group(matcher.groupCount())}")

           // for(index in 1 .. matcher.groupCount()) {
            //    logger.error("group$index=${matcher.group(index)}")
           // }
        }
    }

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
        val dir = File("U:/Observer/1")

        dir.listFiles { f ->
            !f.isDirectory
        }.forEach {
            val text = it.readText(Charset.forName("windows-1251"))

            val newText = text
                    .replace("2020-10-19T", "2020-11-09T")
                    .replace("2020-10-21T", "2020-11-09T")
                    .replace("2020-10-22T", "2020-11-09T")

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