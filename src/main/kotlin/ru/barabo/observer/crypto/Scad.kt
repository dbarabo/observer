package ru.barabo.observer.crypto

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.Cmd.CERT_FOLDER
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.resources.ResourcesManager
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.io.IOException

object Scad {
    private val logger = LoggerFactory.getLogger(Scad::class.java)!!

    private const val CRYPTO_PATH = "C:/progra~1/MDPREI/spki"

    private const val CRYPTO_PROGRAM = "spki1utl.exe"

    private const val DEF_PROFILE = "FEDERAL"


    private const val CRYPTO_FULL_PATH = "$CRYPTO_PATH/$CRYPTO_PROGRAM"

    init {
        initCertFiles()
    }

    private fun initCertFiles() {
        if(initConfig() ) {
            initFiles()
        }
    }

    private fun initConfig(): Boolean {

        val resPath = "/cert/${TaskMapper.buildInfo.build}/$PKI_CONF_FILE"

        return ResourcesManager.copyFromJar(File(Cmd.JAR_FOLDER), resPath) != null
    }

    private fun initFiles() {
        CERT_FOLDER.byFolderExists()

        for (certType in CertificateType.values() ) {

            val resPath = "/cert/${certType.certFile}"

            ResourcesManager.copyFromJar(File(CERT_FOLDER), resPath)
        }
    }

    private const val PKI_CONF_FILE = "pki1.conf"

    private fun encodeCommand(sourceFile: String, encodeFile: String, certificateType: CertificateType): String {

        val certificate = certificateType.certFile

        return "$CRYPTO_FULL_PATH -encrypt -in $sourceFile -out $encodeFile -reclist $CERT_FOLDER\\$certificate -profile $DEF_PROFILE"
    }

    private fun decodeCommand(sourceFile: String, decodeFile: String): String =
            "$CRYPTO_FULL_PATH -decrypt -in $sourceFile -out $decodeFile -profile $DEF_PROFILE"

    private fun signCommand(sourceFile: String, signFile: String): String =
            "$CRYPTO_FULL_PATH -sign -data $sourceFile -out $signFile -profile $DEF_PROFILE"

    private fun unSignCommand(sourceFile: String, unsignFile: String): String =
            "$CRYPTO_FULL_PATH -verify -in $sourceFile -out $unsignFile -delete -1 -profile $DEF_PROFILE"

    private fun processCmd(cmd: String, file: File): File {
        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("processCmd", e)

            if (!TaskMapper.isAfinaBase()) return file

            throw IOException(e.message)
        }

        if(!file.exists()) {
            throw IOException("file not found ${file.absolutePath}")
        }
        return file
    }

    @Throws(IOException::class)
    fun encode(source: File, toEncode: File, certificateType: CertificateType): File {
        val cmd = encodeCommand(source.absolutePath, toEncode.absolutePath, certificateType)

        return processCmd(cmd, toEncode)
    }

    @Throws(IOException::class)
    fun decode(source: File, toDecode: File): File {
        val cmd = decodeCommand(source.absolutePath, toDecode.absolutePath)

        return processCmd(cmd, toDecode)
    }

    @Throws(IOException::class)
    fun sign(source: File, signed: File): File {

        val cmd = signCommand(source.absolutePath, signed.absolutePath)

        val result = processCmd(cmd, signed)

        if (!TaskMapper.isAfinaBase() && !signed.exists()) {
            return source.copyTo(signed)
        }

        return result
    }

    @Throws(IOException::class)
    fun unSign(source: File, toUnsign: File): File {

        val cmd = unSignCommand(source.absolutePath, toUnsign.absolutePath)

        return processCmd(cmd, toUnsign)
    }
}

enum class CertificateType(val certFile: String) {
    FNS("reclistFns.txt"),
    FNS_FSS("reclistFnsFss.txt"),
    FTS("reclistFts.txt"),
    FTS_VAL("reclistValAll.txt"),
    FTS_CB181U("reclistFtsCb181U.txt"),
    FSFM("reclistFsfm.txt")
}