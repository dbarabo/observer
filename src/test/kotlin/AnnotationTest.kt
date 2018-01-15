
import org.junit.Test
import org.slf4j.LoggerFactory
import java.util.regex.Pattern


class AnnotationTest {

    private val logger = LoggerFactory.getLogger(AnnotationTest::class.java)

    @Test
    fun testRegExp() {

        val str1 = "KWTFCB_BNP1_PNO10507717_251020180109_000072_20180110_0021.XML"

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