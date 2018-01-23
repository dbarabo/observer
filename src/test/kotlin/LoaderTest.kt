
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadCtlMtl
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadObi
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadObr
import ru.barabo.observer.config.barabo.plastic.turn.task.LoadRestAccount
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

        val elem = Elem(File("C:/КартСтандарт/test/OBI_20170606_060212_0226_GC_FEE"), LoadObi, Duration.ZERO)

        LoadObi.execute(elem)
    }

    //@Test
    fun loadObr() {
        val elem = Elem(File("C:/КартСтандарт/test/OBR_20171205_161447_0226"), LoadObr, Duration.ZERO)

        elem.task?.execute(elem)
    }

    @Test
    fun loadCtlMtl() {
        val elem = Elem(File("C:/КартСтандарт/test/MTL20180123_0226.0001"), LoadCtlMtl, Duration.ZERO)

        elem.task?.execute(elem)
    }

}