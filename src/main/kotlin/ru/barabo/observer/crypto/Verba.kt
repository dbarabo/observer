package ru.barabo.observer.crypto

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.io.IOException

object Verba {

    private val logger = LoggerFactory.getLogger(Verba::class.java)!!

    private const val VERBA_PATH = "C:/progra~1/mdprei/-ow~1"

    private const val VERBA_PROGRAM = "FColseOW.exe"

    private val VERBA_FILE = File("$VERBA_PATH/$VERBA_PROGRAM")

    private const val SIGNER_PROGRAM = "wftesto.exe"

    private val SIGNER_FULL_PATH = "${Cmd.LIB_FOLDER}/$SIGNER_PROGRAM"

    /**
     * NOT USED BEFORE 2017-12-21
     */
    //private val CBR_550P_SIGN_KEY = "700794200901"

    /**
     * NOT USED BEFORE 2017-12-21
     */
    //private val CBR_550P_SIGN_DISK = "G:\\"


    private const val CBR_SIGN_KEY = "717194100510"

    private const val CBR_SIGN_DISK = "a:\\"

    private const val BARABO_SIGN_KEY = "717194100517"

    private const val BARABO_SIGN_DISK = "a:\\"

    private fun cmdSign(fromFile :File, signedFile :File, keyDisk :String, keyNumber :String) =
            "$SIGNER_FULL_PATH s ${fromFile.absolutePath} ${signedFile.absolutePath} $keyDisk $keyNumber"

    private fun cmdUnSign(file :File) = "$SIGNER_FULL_PATH u ${file.absolutePath}"


    private fun tempFolder() = Cmd.tempFolder("c")

    fun signByCbr(file :File) :File = signFile(file, CBR_SIGN_DISK, CBR_SIGN_KEY)

    fun signByBarabo(file :File) :File = signFile(file, BARABO_SIGN_DISK, BARABO_SIGN_KEY)

    private fun checkerVerba() {
        if(!VERBA_FILE.exists()) {

            if (TaskMapper.isAfinaBase()) {
                throw IOException("Verba not found")
            }
        }
    }

    @Throws(IOException::class)
    private fun signFile(file :File, keyDisk :String, keyNumber :String) :File {
        checkerVerba()

        val tempFolder = tempFolder()

        val tempFile = File("${tempFolder.absolutePath}/${file.name}")

        if(!file.renameTo(tempFile)) {
            tempFolder.deleteFolder()
            throw IOException("file is not renamed ${file.absolutePath}")
        }

        val cmd = cmdSign(tempFile, file, keyDisk, keyNumber)

        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("signFile", e)

            tempFile.renameTo(file)
            tempFolder.deleteFolder()
            throw IOException(e.message)
        }

        tempFolder.deleteFolder()

        if(!file.exists()) throw IOException("file is not signed ${file.absolutePath}")

        return file
    }

    fun signBaraboAndCrypto(folderCrypto :File, mask :String = "*.*") {

        folderCrypto.listFiles()?.filter { !it.isDirectory }?.forEach { signByBarabo(it) }

        cryptoFolder(folderCrypto, mask)
    }

    fun cryptoFolder(folderCrypto :File, mask :String = "*.*") {
        checkerVerba()

        cryptoUncryptoAllFolder("crypt", folderCrypto, mask)
    }

    fun unCryptoAndUnSigned(folderUnCrypto :File, mask :String = "*.*") {

        unCrypto(folderUnCrypto, mask)

        folderUnCrypto.listFiles()?.filter { !it.isDirectory }?.forEach { unSignFile(it) }
    }

    fun unCrypto(folderUnCrypto :File, mask :String = "*.*") {
        checkerVerba()

        cryptoUncryptoAllFolder("decrypt", folderUnCrypto, mask)
    }

    private fun cryptoUncryptoAllFolder(command :String, folder :File, mask :String = "*.*") {

        val scriptFile = createScriptFile(command, folder, mask)

        val cmd = "$VERBA_PATH/$VERBA_PROGRAM /@${scriptFile.absolutePath}"

        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("cryptoUncryptoAllFolder", e)

            scriptFile.delete()

            throw IOException(e.message)
        }
        if(!scriptFile.delete()) {
            logger.error("cryptoUncryptoAllFolder file is not delete file=${scriptFile.absolutePath}")
        }
    }

    fun unSignFile(file :File) :File {

        Cmd.execCmd(cmdUnSign(file))

        return file
    }

    fun unCryptoFile(file :File) :File {

        return cryptoUncrypto(file, "decrypt")
    }

    private fun cryptoUncrypto(file :File, command :String) :File {
        checkerVerba()

        val tempFolder = tempFolder()

        val tempFile = File("${tempFolder.absolutePath}/${file.name}")

        if(!file.renameTo(tempFile)) {
            tempFolder.deleteFolder()
            throw IOException("file is not renamed ${file.absolutePath}")
        }

        val scriptFile = createScriptFile(command, tempFolder, tempFile.name)

        val cmd = "$VERBA_PATH/$VERBA_PROGRAM /@${scriptFile.absolutePath}"

        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("cryptoUncrypto", e)
            scriptFile.delete()
            throw IOException(e.message)
        }

        scriptFile.delete()

        if(!tempFile.renameTo(file)) {
            tempFolder.deleteFolder()

            throw IOException("file is not created ${file.absolutePath}")
        }

        tempFolder.deleteFolder()

        return file
    }

    fun cryptoFile(file :File) :File {

        return cryptoUncrypto(file, "crypt")
     }

    private fun createScriptFile(command :String, folderUnCrypto :File, mask :String) :File {

        val fileScript = File("$VERBA_PATH/${folderUnCrypto.name}.scr")

        val bodyScript = "$command ${folderUnCrypto.absolutePath}\\$mask\n\rStart\n\rClose"

        fileScript.writeText(bodyScript)

        return fileScript
    }
}