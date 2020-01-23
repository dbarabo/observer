
import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.gui.startLaunch
import java.io.File

class GuiTest {

    private val logger = LoggerFactory.getLogger(GuiTest::class.java)

    //@Test
    fun firstTest() {
        startLaunch(Array(0, {""}))
    }

    //@Test
    fun changeDir() {
        logger.error("CURRENT_BEFORE=${File( "." ).canonicalPath}")

        Cmd.execDos("C: && cd C:\\Temp\\doc\\xsd && git add . && git commit -m \"autocommit\" ")

        logger.error("CURRENT_AFTER=${File( "." ).canonicalPath}")
    }

}