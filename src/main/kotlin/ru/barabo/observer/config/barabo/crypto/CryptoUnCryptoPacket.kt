package ru.barabo.observer.config.barabo.crypto

import ru.barabo.cmd.Cmd
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.ActionTask
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import ru.barabo.observer.store.derby.StoreSimple
import java.io.File
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

open class CryptoUnCryptoPacket(private val name :String, private val cryptoFun :(File, String)->Unit) : ActionTask, Executor {

    override fun actionTask(): ActionTask = this

    override fun name(): String = "$name пакет"

    override fun config(): ConfigTask = CryptoConfig

    override val accessibleData: AccessibleData = AccessibleData(WeekAccess.ALL_DAYS,
    false, LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(5))

    override fun findAbstract(): Executor? = null

    override fun execute(elem: Elem): State {

        val folder = elem.getFile()

        if (!folder.exists()) throw IOException("folder not found ${folder.absolutePath}")

        cryptoFun(folder, "*.*") //Verba.unCryptoAndUnSigned(folder)

        val target = File(elem.target)

        if(!target.exists()) {
            target.mkdirs()
        }

        folder.listFiles()?.filter { !it.isDirectory }?.forEach {
            val newFile = File("${target.absolutePath}/${it.name}")
            it.copyTo(newFile, true)
            it.delete()
        }

        folder.delete()

        return State.OK
    }

    fun addFileToPacket(file :File, folderTo :File, isMove :Boolean = true, newExt :String? = null) {

        val elem = StoreSimple.firstItem(this, State.NONE, LocalDateTime.now().minusSeconds(5), folderTo.absolutePath)
                ?: createPacket(folderTo)

        val newFileName = newExt?.let { "${file.nameWithoutExtension}.$newExt" } ?:file.name

        val newFile = File("${elem.getFile().absolutePath}/$newFileName")

        file.copyTo(newFile, true)

        if(isMove) {
            file.delete()
        }
    }

    private fun createPacket(folderTo :File) :Elem {

        val tempFolder = Cmd.tempFolder(folderTo.name)

        val newElem = Elem(tempFolder, this,  Duration.ofMinutes(5), folderTo.absolutePath)

        StoreSimple.save(newElem)

        return newElem
    }
}