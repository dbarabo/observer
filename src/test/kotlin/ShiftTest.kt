
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.correspond.task.UNSIGN_PATH
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
    fun testDecodeBase4() {
        val x = "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"

        val decode = Base64.getDecoder().decode(x)

        val decodeFile =  File("C:\\311-П\\x.zip")

        decodeFile.writeBytes(decode)
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