
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.barabo.crypto.task.LoadBik
import ru.barabo.observer.config.barabo.crypto.task.LoadRateThb
import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.barabo.plastic.release.add.OutApplicationData
import ru.barabo.observer.config.barabo.plastic.release.task.GetOiaConfirm
import ru.barabo.observer.config.barabo.plastic.release.task.OutRegisterAquiringMonth
import ru.barabo.observer.config.barabo.plastic.release.task.OutSmsData
import ru.barabo.observer.config.barabo.plastic.turn.task.*
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.ptkpsd.task.Load101FormXml
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.EsProcess
import ru.barabo.observer.config.task.info.InfoHtmlData
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.time.Duration
import java.time.LocalDate

class LoaderTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    @Before
    fun initTestBase() {
        TaskMapper.init("BARABO", "TEST")
    }

    //@Test
    fun loadRestAccount() {

        val elem = Elem(File("C:/КартСтандарт/test/ACC_20180119_062624_0226"), LoadRestAccount, Duration.ZERO)

        LoadRestAccount.execute(elem)
    }

    //@Test
    fun loadObi() {

        val elem = Elem(File("C:/КартСтандарт/test/OBI_20180111_060206_0226_GC_FEE"), LoadObi, Duration.ZERO)

        LoadObi.execute(elem)
    }

    //@Test
    fun loadObr() {
        val elem = Elem(File("C:/КартСтандарт/test/OBR_20171205_161447_0226"), LoadObr, Duration.ZERO)

        elem.task?.execute(elem)
    }


    //@Test
    fun loadAfp() {
        val elem = Elem(File("C:/КартСтандарт/test/AFP20180924_0226.0013"), LoadAfp, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun outIbi() {
        val elem = Elem(task = OutIbi)

        elem.task?.execute(elem)
    }


    //@Test
    fun execObi() {
        val elem = Elem(idElem = 1172320580, task = ExecuteObi)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadPno() {
        //val elem = Elem(File("C:/440-П/test/PNO10507717_253720180131_000066.xml"), PnoLoader, Duration.ZERO)

        //val elem = Elem(File("C:/440-П/test/ROO10507717_253620180131_000015.xml"), RooLoader, Duration.ZERO)

        //val elem = Elem(File("C:/440-П/test/ZSV10507717_250820180131_000024.xml"), ZsvLoader, Duration.ZERO)

        val elem = Elem(File("C:/440-П/test/RPO10507717_254320180130_000753.xml"), RpoLoader, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadZsv() {
        //val elem = Elem(File("C:/440-П/test/ZSV10507717_132720180130_000152.xml"), ZsvLoader, Duration.ZERO)

        val elem = Elem(File("C:/440-П/test/ZSV10507717_132720180130_000164.xml"), ZsvLoader, Duration.ZERO)

        elem.task?.execute(elem)
    }



    //@Test
    fun exec440p() {

        //val elem = Elem(idElem = 1172496436, task = Process440p)

        //val elem = Elem(idElem = 1172496444, task = Process440p)

        val elem = Elem(idElem = 1172496446, task = Process440p)

        elem.task?.execute(elem)
    }


    //@Test
    fun outPb() {
        val elem = Elem(idElem = 1172496441, task = PbSaver)

        elem.task?.execute(elem)
    }

    //@Test
    fun outBos() {
        val elem = Elem(idElem = 1172496442, task = RestSaver)

        elem.task?.execute(elem)
    }

    //@Test
    fun outBvsExtract() {
        //val elem = Elem(idElem = 1172496449, task = ExtractMainSaver)

        val elem = Elem(idElem = 1172496453, task = ExtractMainSaver)

        elem.task?.execute(elem)
    }


    //@Test
    fun oiaProcess() {
        //val elem = Elem(idElem = 1172496453, task = GetOiaConfirm)

        val elem = Elem(File("H:/КартСтандарт/in/OIA_20180202_054637_0226"), GetOiaConfirm, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun ociProcess() {
        //val elem = Elem(idElem = 1172496453, task = GetOiaConfirm)

        val elem = Elem(File("H:/КартСтандарт/in/OIA_20180202_054637_0226"), GetOiaConfirm, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun checkOpenArchiveDay() {

        val elem = Elem(idElem = 57558470, name = "16.02.2018", task = CheckOpenArchiveDay)

        elem.task?.execute(elem)
    }


    //@Test
    fun execOpenArchiveDay() {

        val elem = Elem(idElem = 0, task = ExecOpenArchiveDay)

        elem.task?.execute(elem)
    }

    //@Test
    fun execLoadBik() {

        val elem = Elem(idElem = 0, task = LoadBik)

        elem.task?.execute(elem)
    }

    @Test
    fun saveOutOutApplicationData() {
        OutApplicationData.execute(idPacket = 1182748836)
    }

    //@Test
    fun saveOutSmsData() {

        val elem = Elem(idElem = 1182782271, task = OutSmsData)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadForm101() {

        //val f101Xml = XmlLoaderF101<F101Xml>().load(File("C:/440-П/test/Ф101_40507717.xml"))

        //logger.error("f101Xml=$f101Xml")

        val elem = Elem(File("C:/440-П/test/Ф101_40507717.xml"), Load101FormXml, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun sentHtmlReport() {
        InfoHtmlData.sendInfo(LocalDate.now(), StoreSimple.getRootElem())
    }

    //@Test
    fun executeOverdraftJuric() {
        val elem = Elem(task = ExecuteOverdraftJuric)

        elem.task?.execute(elem)
    }

    //@Test
    fun executeGroupRateLoan() {
        val elem = Elem(task = ExecuteGroupRateLoan)

        elem.task?.execute(elem)
    }

   // @Test
    fun load550P() {
        EsProcess.process(File("C:/440-П/test/CB_ES550P_20180423_002.XML"))
    }

    //@Test
    fun loadCtlMtl() {
        val elem = Elem(File("C:/КартСтандарт/test/CTL20181026_0226.0001"), LoadCtlMtl, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun execAfp() {
        val elem = Elem(idElem = 1179223102, task = ExecuteAfp)

        elem.task?.execute(elem)
    }

    //@Test
    fun execCtl() {
        val elem = Elem(idElem = 1181972978, task = ExecuteCtlMtl)

        elem.task?.execute(elem)
    }

    //@Test
    fun execCheckActializationEntryDouble() {
        val elem = Elem(idElem = 57425917, task = CheckActializationEntryDouble)

        elem.task?.execute(elem)
    }

    //@Test
    fun execLoadRateThb() {
        val elem = Elem(task = LoadRateThb)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadRateThb() {
        logger.error("thbRate=${LoadRateThb.thbRate()}")
    }

    //@Test
    fun outRegisterAquiring() {

        val elem = Elem(idElem = 1181803901, task = OutRegisterAquiring)

        elem.task?.execute(elem)
    }

    //@Test
    fun outRegisterAquiringMonth() {
        val elem = Elem(task = OutRegisterAquiringMonth)

        elem.task?.execute(elem)
    }

    //@Test
    fun outRestCheck() {

        val elem = Elem(task = OutRestCheck)

        elem.task?.execute(elem)
    }

    //@Test
    fun testToIntOrNull() {

        val x = "ACCOUNT[2017810058]".toIntOrNull()

        logger.error("x=$x")
    }

    //@Test
    fun cecReportProcess() {

        val elem = Elem(task = CecReportProcess, path = "X:\\ЦИК\\2018.10.05\\Запрос", name = "F1027700466640_021018_Z_0039.xml")

        elem.task?.execute(elem)
    }

    //@Test
    fun parseIntLong() {
       val value = (File("SFF010507717_254020181106_002100001800002869_700.xml").nameWithoutExtension
                .substringAfterLast("0000").substringBefore('_').toLong() % 1000000).toInt()

        logger.error("value=$value")
    }


    //@Test
//    fun testSubstring() {
//
//        val responseDateTime = LocalDateTime.now()
//
//        val fileRequest = File("X:\\ЦИК\\2018.10.05\\Запрос/F1027700466640_021018_Z_0039.xml")
//
//        val requestDate = fileRequest.nameWithoutExtension.substringAfter('_').substringBefore('_')
//        logger.error("requestDate=$requestDate")
//
//        val requestNumber = fileRequest.nameWithoutExtension.substringAfter("_Z_")
//        logger.error("requestNumber=$requestNumber")
//
//        val responseFile = File("${CecReportProcess.OUR_CODE}_${responseDateTime.formatDateTime()}_K_${requestNumber}_1000_${CecReportProcess.CEC_CODE}.xml")
//        logger.error("responseFile=$responseFile")
//
//        val textResponse = CecReportProcess.emptyTicketTemplate(responseDateTime.formatDateTime(), requestNumber, requestDate, responseDateTime.formatDateDDMMYYYY())
//        logger.error("textResponse=$textResponse")
//    }


}