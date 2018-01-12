
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalTime
import java.util.regex.Pattern
import kotlin.concurrent.timer

class TaskTest {

    val logger = LoggerFactory.getLogger(TaskTest::class.java)



    private fun isWorkTime(workTimeFrom :LocalTime, workTimeTo :LocalTime) :Boolean = if(workTimeFrom < workTimeTo) {
        (workTimeFrom <= LocalTime.now() && LocalTime.now() <= workTimeTo)
    } else {
        (workTimeFrom <= LocalTime.now() || LocalTime.now() <= workTimeTo)
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

    @Test
    fun testTimer() {
        timer("test", false, 0, 3_000) {logger.error(LocalTime.now().toString())}

        timer("TIMER2", false, 0, 2_000) {logger.error(LocalTime.now().toString())}

        Thread.sleep(12_000);
    }


}