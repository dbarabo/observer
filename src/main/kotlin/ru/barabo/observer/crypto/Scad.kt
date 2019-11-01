package ru.barabo.observer.crypto

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.Cmd.CERT_FOLDER
import java.io.File
import java.io.IOException

object Scad {
    private val logger = LoggerFactory.getLogger(Scad::class.java)!!

    private const val CRYPTO_PATH = "C:/progra~1/MDPREI/spki"

    private const val CRYPTO_PROGRAM = "spki1utl.exe"

    private const val DEF_PROFILE = "FEDERAL"

    private const val CRYPTO_FULL_PATH = "$CRYPTO_PATH/$CRYPTO_PROGRAM"

    private fun encodeCommand(sourceFile: String, encodeFile: String, certificateType: CertificateType): String {

        val certificate = getCertificate(certificateType)

        return "$CRYPTO_FULL_PATH -encrypt -in $sourceFile -out $encodeFile -reclist $CERT_FOLDER\\$certificate -profile $DEF_PROFILE"
    }

    private fun decodeCommand(sourceFile: String, decodeFile: String): String =
            "$CRYPTO_FULL_PATH -decrypt -in $sourceFile -out $decodeFile -profile $DEF_PROFILE"

    private fun signCommand(sourceFile: String, signFile: String): String =
            "$CRYPTO_FULL_PATH -sign -data $sourceFile -out $signFile -profile $DEF_PROFILE"

    private fun unSignCommand(sourceFile: String, unsignFile: String): String =
            "$CRYPTO_FULL_PATH -verify -in $sourceFile -out $unsignFile -delete -1 -profile $DEF_PROFILE"

    private fun getCertificate(certificateType: CertificateType): String =
            when(certificateType) {
                CertificateType.FNS -> "reclistFns.txt"
                CertificateType.FNS_FSS -> "reclistFnsFss.txt"
                CertificateType.FTS -> "reclistFts.txt"
                else -> throw Exception("not found for $certificateType")
            }

    private fun processCmd(cmd: String, file: File): File {
        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("processCmd", e)

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

        return processCmd(cmd, signed)
    }

    @Throws(IOException::class)
    fun unSign(source: File, toUnsign: File): File {

        val cmd = unSignCommand(source.absolutePath, toUnsign.absolutePath)

        return processCmd(cmd, toUnsign)
    }
}

enum class CertificateType {
    FNS,
    FNS_FSS,
    FTS
}