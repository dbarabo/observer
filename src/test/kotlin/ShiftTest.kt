
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadCtlMtl
import ru.barabo.observer.config.skad.acquiring.task.LoadPaymentWeechatXlsx
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.Shift
import ru.barabo.observer.store.TaskMapper
import ru.barabo.xlsx.SheetReader
import ru.barabo.xlsx.byString
import java.io.File
import java.net.InetAddress
import java.nio.charset.Charset
import java.time.Duration

class ShiftTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    @Before
    fun initTestBase() {
        TaskMapper.init("TEST", "AFINA")

        com.sun.javafx.application.PlatformImpl.startup {}
    }


    @Test
    fun loadPaymentWeechatXlsx() {
        //val elem = Elem(File("C:/Temp/2/paymentsacq_daily_2020.07.31.xlsx"), LoadPaymentWeechatXlsx, Duration.ZERO)

        val elem = Elem(File("C:/Temp/2/paymentsacq_daily_2020.07.29.xlsx"), LoadPaymentWeechatXlsx, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun xlsxReadTest() {

        val reader = SheetReader(File("C:/Temp/2/paymentsacq_daily_2020.07.31.xlsx") )

        val data = reader.readSheet()

        for (row in data) {
            logger.error("row index = ${row.rowIndex} count = ${row.columns.size}")

            for(cell in row.columns) {
                logger.error("cell index = ${cell.columnIndex} type = ${cell.cellType} value = ${cell.byString(cell.columnIndex == 1)}")

                // logger.error("cell String = ${cell.stringCellValue}")
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