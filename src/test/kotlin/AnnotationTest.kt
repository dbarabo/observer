
import org.slf4j.LoggerFactory
import java.util.regex.Pattern
import kotlin.concurrent.timer


class AnnotationTest {

    private val logger = LoggerFactory.getLogger(AnnotationTest::class.java)

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

        val isFindDocx :Boolean = pattern.matcher("ZLB_0226_20190218.docx")?.matches()?:false?:false

        val isFindDoc :Boolean = pattern.matcher("ZLB_0226_20190218.doc")?.matches()?:false?:false

        val isFindXlsx :Boolean = pattern.matcher("ZLB_0226_20190218.xlsx")?.matches()?:false?:false

        val isFindXls :Boolean = pattern.matcher("ZLB_0226_20190218.xls")?.matches()?:false?:false

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