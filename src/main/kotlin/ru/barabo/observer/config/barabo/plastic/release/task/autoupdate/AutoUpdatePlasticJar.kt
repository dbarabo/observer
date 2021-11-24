package ru.barabo.observer.config.barabo.plastic.release.task.autoupdate

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.net.URI
import java.time.LocalDateTime
import java.util.*

abstract class AutoUpdatePlasticJar : Executor, ActionTask {

    private val logger = LoggerFactory.getLogger(AutoUpdatePlasticJar::class.java)

    override fun config(): ConfigTask = PlasticOutSide // CryptoConfig

    protected abstract fun params(): Array<Any?>?

    override fun actionTask(): ActionTask = this

    override fun findAbstract(): Executor? {

        val count = try {
            AfinaQuery.selectCursor(SELECT, params() )
                .filter {(it[2] as? String)?.substringBeforeLast('.', "6")?.last() != '5'}
                .map {Elem(idElem = (it[0] as? Number)?.toLong(),
                        name = it[1] as String,
                        path = (it[2] as? String) ?: "",
                        task = this,
                        executed = calcExecutedByState(it[3] as? Number)  )}

                    .filter { StoreSimple.addNotExistsByIdElem(it) }.count()
        } catch (e: Exception) {
            logger.error("findAbstract", e)

            0
        }

        return if(count == 0) null else this
    }

    override fun execute(elem: Elem): State {

        if(!NEW_PLASTIC_JAR.exists()) return State.NONE

        val oldJar = elem.remoteFilePath()

        if(!oldJar.name.contains(".jar", ignoreCase = true) ) {
            AfinaQuery.execute(UPDATE_STATE, elem.paramsUpdateState())

            return State.OK
        }

        return if((!oldJar.exists()) || oldJar.delete()) {
            NEW_PLASTIC_JAR.copyTo( File("${oldJar.parent}\\$PROGRAM_JAR"), true )

            AfinaQuery.execute(UPDATE_STATE, elem.paramsUpdateState())

            State.OK
        } else State.NONE
    }

    companion object {
        private const val SELECT = "{ ? = call od.PTKB_PLASTIC_AUTO.selectForUpdateVesionPrograms( ?, ? ) }"

        const val PROGRAM_NAME = "PLASTIC.JAR"

        const val IS_CRITICAL_TRUE = 1

        const val IS_CRITICAL_FALSE = 0

        private const val PROGA_STATE_EXIT = 2

        private const val PROGRAM_JAR = "plastic.jar"

        private val NEW_PLASTIC_JAR = File("${Cmd.LIB_FOLDER}/$PROGRAM_JAR")

        private const val UPDATE_STATE = "{ call od.PTKB_PLASTIC_AUTO.afterAutoUpdateState(?, ?, ?) }"
    }

    private fun calcExecutedByState(state: Number?): LocalDateTime =
            if( (state?.toInt()?:0) == PROGA_STATE_EXIT) LocalDateTime.now()
            else LocalDateTime.now().plusSeconds(5)



    private fun Elem.paramsUpdateState(): Array<Any?> = arrayOf(PROGRAM_NAME, name, path)
}

fun Elem.remoteFilePath(): File  = if(name.isRemoteNetDisk()) File(name) else File(URI(createNetPath(name, path)))

fun String.isRemoteNetDisk() = "\\/HIJKPSTVWXZ".contains(substring(1..1).uppercase(Locale.getDefault())) || "\\/HIJKPSTVWXZ".contains(
    substring(0..0).uppercase(Locale.getDefault())
)

private fun createNetPath(localFullPath: String, ipAddress: String) =  "file://///$ipAddress/${localFullPath.addAdmin()}"  //"\\\\$ipAddress\\${localFullPath.substring(1).addAdmin()}"

private fun String.addAdmin() =  this.replace(':', '$')