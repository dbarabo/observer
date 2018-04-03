
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.store.Shift
import java.net.InetAddress

class ShiftTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    @Test
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