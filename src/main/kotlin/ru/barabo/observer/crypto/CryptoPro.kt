package ru.barabo.observer.crypto

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import java.io.File
import java.io.IOException

object CryptoPro {

    private val logger = LoggerFactory.getLogger(CryptoPro::class.java)!!

    private const val CRYPTO_PATH = "C:/progra~1/crypto~1/CSP"

    private const val CRYPTO_PROGRAM = "csptest.exe"

    private const val CRYPTO_FULL_PATH = "$CRYPTO_PATH/$CRYPTO_PROGRAM"

    private const val CERTIFICATE_MY = "-my brykina@ptkb.ru"

    private const val CERTIFICATE_TO = "-cert support@nbki.ru"

    private fun signCommand(sourceFile: String, signedFile :String) =
            "$CRYPTO_FULL_PATH -sfsign -sign -detached -add -in $sourceFile -out $signedFile $CERTIFICATE_MY"


    private fun encodeCommand(sourceFile: String, encodeFile :String) =
            "$CRYPTO_FULL_PATH -sfenc -encrypt -in $sourceFile -out $encodeFile $CERTIFICATE_TO"

    private fun decodeCommand(sourceFile: String, decodeFile :String) =
            "$CRYPTO_FULL_PATH -sfenc -decrypt -in $sourceFile -out $decodeFile $CERTIFICATE_MY"

    @Throws(IOException::class)
    fun sign(source: File, signed : File) :File {

        val cmd = signCommand(source.absolutePath, signed.absolutePath)

        return processCmd(cmd, signed)
    }

    private fun processCmd(cmd :String, file :File) :File {
        try {
            Cmd.execCmd(cmd)
        } catch (e :Exception) {
            logger.error("sign", e)

            throw IOException(e.message)
        }

        if(!file.exists()) {
            throw IOException("file not found ${file.absolutePath}")
        }

        return file
    }

    @Throws(IOException::class)
    fun encode(source: File, encoded : File) :File {
        val cmd = encodeCommand(source.absolutePath, encoded.absolutePath)

        return processCmd(cmd, encoded)
    }

    @Throws(IOException::class)
    fun decode(source: File, decoded : File) :File {

        val cmd = decodeCommand(source.absolutePath, decoded.absolutePath)

        return processCmd(cmd, decoded)
    }
}