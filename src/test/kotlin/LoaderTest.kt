
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.barabo.p440.task.PbSaver
import ru.barabo.observer.config.barabo.p440.task.Process440p
import ru.barabo.observer.config.barabo.p440.task.RestSaver
import ru.barabo.observer.config.barabo.p440.task.RpoLoader
import ru.barabo.observer.config.barabo.plastic.turn.task.*
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.time.Duration

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
    fun loadCtlMtl() {
        //val elem = Elem(File("C:/КартСтандарт/test/CTL20180125_0226.0001"), LoadCtlMtl, Duration.ZERO)

        File("C:/КартСтандарт/test/").listFiles { f -> !f.isDirectory && f.name.indexOf("CTL")==0}.forEach {

            val elem = Elem(it, LoadCtlMtl, Duration.ZERO)

            elem.task?.execute(elem)
        }
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
    fun execCtl() {
        val elem = Elem(idElem = 1172320200, task = ExecuteCtlMtl)

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
    fun execRpo() {

        val elem = Elem(idElem = 1172496436, task = Process440p)

        elem.task?.execute(elem)
    }


    //@Test
    fun outPb() {
        val elem = Elem(idElem = 1172496441, task = PbSaver)

        elem.task?.execute(elem)
    }

    @Test
    fun outBos() {
        val elem = Elem(idElem = 1172496442, task = RestSaver)

        elem.task?.execute(elem)
    }

}