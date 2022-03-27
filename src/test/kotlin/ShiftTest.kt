
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.skad.acquiring.task.LoadPaymentWeechatXlsx
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.Shift
import ru.barabo.xlsx.SheetReader
import ru.barabo.xlsx.byString
import java.io.File
import java.net.InetAddress
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern

class ShiftTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    //@Before
    fun initTestBase() {
        //TaskMapper.init("TEST", "AFINA")

        com.sun.javafx.application.PlatformImpl.startup {}
    }

    //@Test
    fun testBeforeLast() {

        val value1 = "TK_1643007015140_KO-21_2022-01-24T09-50-15_1_F0409202_ies1.2ko717.xml.165408"

        logger.error("value1=${value1.substringBeforeLast(".")}")
    }



    //@Test
    fun testFormat() {

        val dayByMoscow = "%02d".format( LocalDateTime.now().minusHours(7).dayOfMonth )

        logger.error("dayByMoscow=$dayByMoscow")
    }

    //@Test
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

        logger.error(InetAddress.getLocalHost().hostName.uppercase(Locale.getDefault()))
    }

    //@Test
    fun firstSplitBracket() {

        val values = "[Корсчета в других банках]+[Средства в КО]".splitByRegexp("\\[(.*?)\\]")
      //  "\\[(.*?)\\]".toRegex())
    //    "\\[(.*?)\\]"

        logger.error("values=$values")
    }

    private fun String.splitByRegexp(regExp: String): List<String> {

        val matcher = Pattern.compile(regExp).matcher(this)

        val list = ArrayList<String>()

        while(matcher.find()) {
            list += matcher.group(1)
        }

        return list
    }

    //@Test
    fun keyValueTest() {
        val sections = LinkedHashMap<Section, Int?>()

        sections[Section("01")] = null
        sections[Section("02")] = null

        fillValue(sections)

        fillMapValue(sections)

        logger.error("sections=$sections")
    }

    private fun fillValue(sections: Map<Section, Int?>) {

        for(keySection in sections.keys) {

            keySection.column = (100*Math.random()).toInt()
        }
    }

    private fun fillMapValue(sections: MutableMap<Section, Int?>) {


        sections.keys.forEach { sections.put(it, (10*Math.random()).toInt() ) }
    }
}

internal data class Section(val name: String, var column: Int? = null)