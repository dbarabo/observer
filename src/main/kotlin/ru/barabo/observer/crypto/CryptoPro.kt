package ru.barabo.observer.crypto

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.store.BuildInfo
import ru.barabo.observer.store.TaskMapper
import java.io.File
import java.io.IOException

object CryptoPro {

    private val logger = LoggerFactory.getLogger(CryptoPro::class.java)!!

    private const val CRYPTO_PATH = "C:/progra~1/crypto~1/CSP"

    private const val CRYPTO_PROGRAM = "csptest.exe"

    private const val CRYPTO_FULL_PATH = "$CRYPTO_PATH/$CRYPTO_PROGRAM"

    private val certificateData: CertificateData = initCertByBuild(TaskMapper.buildInfo)

    private fun signCommand(sourceFile: String, signedFile: String) =
            "$CRYPTO_FULL_PATH -sfsign -sign -detached -add -in $sourceFile -out $signedFile -my ${certificateData.my}"

    private fun encodeCommand(sourceFile: String, encodeFile: String) =
            "$CRYPTO_FULL_PATH -sfenc -encrypt -in $sourceFile -out $encodeFile -cert ${certificateData.to}"

    private fun decodeCommand(sourceFile: String, decodeFile: String) =
            "$CRYPTO_FULL_PATH -sfenc -decrypt -in $sourceFile -out $decodeFile -my ${certificateData.my} ${passwordIfExists(certificateData.password)}"

    private fun passwordIfExists(password: String?): String = password?.let { "-password $it" } ?: ""

    private fun unsignCommand(signedFile: String, unSignedFile: String, cert: String) =
            "$CRYPTO_FULL_PATH -sfsign -verify -in $signedFile -out $unSignedFile -my $cert"

    @Throws(IOException::class)
    fun unsign(signed: File, unSigned: File): File {

        if(unSigned.exists()) {
            unSigned.delete()
        }

        for(cert in certificateData.unsignList) {
            val cmd = unsignCommand(signed.absolutePath, unSigned.absolutePath, cert)

            val fileUnsign = try {
                processCmd(cmd, unSigned)
            }  catch (exception: IOException) {
                null
            }

            if(fileUnsign != null) return fileUnsign
        }

        throw IOException("file ${signed.absolutePath} is not unsigned for unsigned ${unSigned.absolutePath}")
    }

    @Throws(IOException::class)
    fun sign(source: File, signed: File): File {

        val cmd = signCommand(source.absolutePath, signed.absolutePath)

        return processCmd(cmd, signed)
    }

    @Throws(IOException::class)
    private fun processCmd(cmd: String, file: File) :File {
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
    fun decode(source: File, decoded: File) :File {

        val cmd = decodeCommand(source.absolutePath, decoded.absolutePath)

        return processCmd(cmd, decoded)
    }
}

data class CertificateData(val my: String, val to: String, val unsignList: List<String> = emptyList(), val password: String? = null)

private fun initCertByBuild(buildInfo: BuildInfo): CertificateData =
    when (buildInfo) {
    BuildInfo.Barabo -> baraboCertificateData()

    BuildInfo.Jzdo -> jzdoCertificateData()

    else ->  throw Exception("for build ${buildInfo.build} certificate setting not found")
    }

private fun baraboCertificateData(): CertificateData = CertificateData(/*"tts@ptkb.ru"*/"antasuk@ptkb.ru", "ais@nbki.ru", listOf("ais@nbki.ru") )

private fun jzdoCertificateData(): CertificateData = CertificateData("88cfad3b29aecb9a723c46bf5a5e55a8d5e7521a"/*"sima@ptkb.ru"*/, "e.krivonosov@cft.ru",
        listOf("e.krivonosov@cft.ru", "e.maslakov@cft.ru"), "12345678")



