
import org.junit.Before
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.barabo.crypto.task.LoadBik
import ru.barabo.observer.config.barabo.crypto.task.LoadRateThb
import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.barabo.plastic.release.task.GetOiaConfirm
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
        val elem = Elem(File("C:/КартСтандарт/test/AFP20180124_0226.0011"), LoadAfp, Duration.ZERO)

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
    fun outRegisterAquiring() {

        val elem = Elem(idElem = 1172032472/*1172458967*/, task = OutRegisterAquiring)

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

    //@Test
    fun saveOutSmsData() {

        val elem = Elem(idElem = 1174165244, task = OutSmsData)

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
        val elem = Elem(File("C:/КартСтандарт/test/CTL20180817_0226.0001"), LoadCtlMtl, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun execCtl() {
        val elem = Elem(idElem = 1179296203, task = ExecuteCtlMtl)

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
}