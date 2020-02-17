
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.archive.Archive
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.crypto.CertificateType
import ru.barabo.observer.crypto.Scad
import ru.barabo.xls.Parser
import ru.barabo.xls.Var
import java.io.File
import java.net.InetAddress
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.regex.Pattern
import kotlin.concurrent.timer

class TaskTest {

    private val logger = LoggerFactory.getLogger(TaskTest::class.java)


    private fun isWorkTime(workTimeFrom :LocalTime, workTimeTo :LocalTime) :Boolean = if(workTimeFrom < workTimeTo) {
        (workTimeFrom <= LocalTime.now() && LocalTime.now() <= workTimeTo)
    } else {
        (workTimeFrom <= LocalTime.now() || LocalTime.now() <= workTimeTo)
    }

    //@Test
    fun scadTest() {

        val source = File("c:\\temp\\source.txt")

        val sign = File("c:\\temp\\source.sgn")

        val encode = File("c:\\temp\\source.vrb")

        val decodeSign = File("c:\\temp\\source.ssgn")

        val unSignAndDecode = File("c:\\temp\\source.unsgns")

        Scad.sign(source, sign)

        Scad.encode(sign, encode, CertificateType.FNS)

        Scad.decode(encode, decodeSign)

        Scad.unSign(decodeSign, unSignAndDecode)
    }

    //@Test
    fun gzipPackUpPackTest() {
        val gzipFile = Archive.packToGZip("c://temp//test.txt.gz", File("c://test.txt"))

        logger.error("gzipFile=$gzipFile")

        val unzipFile = Archive.unPackFromGZip("c://temp//test.txt.gz", "test.txt")

        logger.error("unzipFile=$unzipFile")
    }

    //@Test
    fun testTime() {
        val from :LocalTime  = LocalTime.of(14, 30)

        val to :LocalTime  = LocalTime.of(17, 50)

        logger.info(isWorkTime(to, from).toString())
    }

    //@Test
    fun testFile() {

        val file = File("D:/distr/0zi07_05.717")

        val text = file.readText()

        logger.info("${text.indexOf("MSCF") == 0}")

        logger.info(text)
    }

    //@Test
    fun testRegExp() {
        val text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<UV>\n" +
                "<TU_OKATO>05</TU_OKATO>\n" +
                "<TU_NAME>ДАЛЬНЕВОСТОЧНОЕ ГУ БАНКА РОССИИ</TU_NAME>\n" +
                "<IDNOR>0021_0000</IDNOR>\n" +
                "<ARH>ARH550P_0021_0000_20171220_001.arj</ARH>\n" +
                "<SIZE_ARH>7943</SIZE_ARH>\n" +
                "<DATE_ARH>20/12/2017</DATE_ARH>\n" +
                "<REZ_ARH>принят</REZ_ARH>\n" +
                "<DATE_UV>20/12/2017</DATE_UV>\n" +
                "<TIME_UV>14:33:30</TIME_UV>\n" +
                "<OPER>Ремарт ПТК-ПСД</OPER>\n" +
                "<TEL_OPER>(423) 240-65-51</TEL_OPER>\n" +
                "</UV>"

        val regex = Pattern.compile("<ARH>(.*?)</ARH>")

        val regexMatcher = regex.matcher(text)

        while (regexMatcher.find()) {
            logger.info(regexMatcher.group(1))
        }
    }

    //@Test
    fun testSplit() {

        val columns = "FNS_POST, FNS_PHONE, FNS_FIO, TYPE_440P, BANK_BIK, BANK_NAME, FNS_CODEID, FNS_NAME, " +
                "MAIN_NUMBER, MAIN_DATE, MAIN_CODE, MAIN_DESCRIPTION, MAIN_SUM, MAIN_TYPE, MAIN_STATUS, ACCOUNTS, " +
                "CARDS, CARDS_CURRENCY, ADD_NUMBER, ADD_DATE, SUB_NUMBER, SUB_DATE, FILE_NAME, ID"

        logger.error(columns.split(",").joinToString(",") { _ -> "?" })
    }

    //@Test
    fun testSubstring() {
        logger.error("1234567890".substring(0, 3))
    }

    //@Test
    fun testTimer() {
        timer("test", false, 0, 3_000) {logger.error(LocalTime.now().toString())}

        timer("TIMER2", false, 0, 2_000) {logger.error(LocalTime.now().toString())}

        Thread.sleep(12_000);
    }

    //@Test
    fun testSubstr() {
        logger.error("22617810705182389250                       222466".substring(0, 32).trim())

        logger.error(("22617810705182389250                       222466".substring(32, 32 + 17).trim().toDouble() / 100).toString())

        logger.error("20180119_06:26:24".substring(0, 17))

        logger.error(Timestamp(SimpleDateFormat("yyyyMMdd_HH:mm:ss").parse(
                "20180119_06:26:24".substring(0, 17)).time).toString())
    }

    //@Test
    fun testDiv() {
        logger.error((("6946559".trim().toLong().toDouble()) / 100).toString())
    }

    //@Test
    fun testUtf8() {
        logger.error("☹☹☹⛔⛔⛔✚✚✚☘☘⛄⛄⛄✖✖✖")


    }

    //@Test
    fun testSplitKortege() {

        for (index in 0..99) {
            logger.error(":${DecimalFormat("00").format(index)}:")
        }
        /*
        val str = "123456789"

        logger.error("substr=${str.substring(1..8)}")
       */
        /*for (index in (4-1) downTo 0) {
            logger.error("index=$index")
        }

        /*
        val a= "".split('.')

        val b = if(a.size > 1) a[1] else ""

        logger.error("a=${a[0]}")
        logger.error("b=$b")*/

         */
    }

    //@Test
    fun getLocalHost() {
        logger.error(InetAddress.getLocalHost().hostName.toUpperCase())
    }

    // @Test
    fun testParser() {

        //val text = "b = 4; c = 5; a = (([b] == 3) and (4 != [c]))"

        val text = "b = 10.5; c = 11.7; a = [c]; c = 5.7; b = [c]; b = 9"

        val vars = ArrayList<Var>()

        val parser = Parser(AfinaQuery)

        val expression = parser.parseExpression(text, vars)

        expression.forEach { logger.error("$it") }
        vars.forEach { logger.error("$it") }

        val result = parser.execExpression(expression)
        logger.error("result=$result")
        vars.forEach { logger.error("$it") }
    }
}