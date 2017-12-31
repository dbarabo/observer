import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.observer.gui.startLaunch

class GuiTest {

    private val logger = LoggerFactory.getLogger(GuiTest::class.java)

    //@Test
    fun firstTest() {
        startLaunch(Array<String>(0, {""}))
    }

}