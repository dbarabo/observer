
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.store.Shift
import java.io.File
import java.net.InetAddress
import java.nio.charset.Charset

class ShiftTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    //@Test
    fun findRow() {
        File("c:/temp/BVD1_ZSV10507717_254320190403_367859_20190410_0021_000001_000001.xml")
                .readLines(Charset.forName("CP1251"))
                .forEach {
                    if(it.contains("<РеквПлат") && !it.contains("НаимПП=")) {
                        logger.error(it)
                    }
                }
    }


    //@Test
    fun firstShifTest() {

//        val primterkombank = Shift.encrypt("primterkombank@gmail.com")
//        logger.error("primterkombank@gmail.com=$primterkombank")
//
//        val dbarabo = Shift.encrypt("dbarabo@gmail.com")
//        logger.error("dbarabo@gmail.com=$dbarabo")
//
//        val ptkbPswd = Shift.encrypt("Sn907369")
//        logger.error("ptkbPswd=$ptkbPswd")

        val dbarabo = Shift.decrypt("9aKALO/eUfC+x7DT1/bs6e9I6+iHYg8JY7KHAyZ3K/E=")
        logger.error("dbarabo=$dbarabo")

        val ptkb = Shift.decrypt("flH6Ibec/wOXYvbJc1u+IwZfOfp1PQMydzVMRRcm3UBF7UkBqViGMg==")
        logger.error("ptkb=$ptkb")

        val pswd = Shift.decrypt("PvxK/Qnz/Mno/sGWDhXT8bsMSLKdDapp")
        logger.error("pswd=$pswd")

        logger.error(InetAddress.getLocalHost().hostName.toUpperCase())
    }
}