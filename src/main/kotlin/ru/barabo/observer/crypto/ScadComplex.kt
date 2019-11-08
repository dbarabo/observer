package ru.barabo.observer.crypto

import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import java.io.File

object ScadComplex {

    fun fullEncode311p(sourceFile: File, encodeFile: File, isAddFss: Boolean = true): File
            = signGzipEncodeFns(sourceFile, encodeFile, isAddFss, "p311")

    fun fullEncode440p(sourceFile: File, encodeFile: File): File = signGzipEncodeFns(sourceFile, encodeFile, false, "p440")

    fun fullDecode440p(sourceFile: File, decodeFile: File): File = decodeUngzipUnsign(sourceFile, decodeFile, "p440")

    private fun signGzipEncodeFns(sourceFile: File, encodeFile: File, isAddFss: Boolean, prefix: String): File {
        val tempFolder = Cmd.tempFolder(prefix)

        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val sign = File("${tempFolder.absolutePath}/${sourceFile.name}_sgn")
        Scad.sign(copySource, sign)

        val signGzip = File("${tempFolder.absolutePath}/${sourceFile.name}_sgz")

        Archive.packToGZip(signGzip.absolutePath, sign)

        val encode = File("${tempFolder.absolutePath}/${encodeFile.name}")

        Scad.encode(signGzip, encode, if(isAddFss) CertificateType.FNS_FSS else CertificateType.FNS)

        encode.copyTo(encodeFile, true)

        tempFolder.deleteFolder()

        return encodeFile
    }

    private fun decodeUngzipUnsign(sourceFile: File, decodeFile: File, prefix: String): File {
        val tempFolder = Cmd.tempFolder(prefix)

        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val decode = File("${tempFolder.absolutePath}/${decodeFile.name}_dec")
        Scad.decode(copySource, decode)

        val unzip = File("${tempFolder.absolutePath}/${decodeFile.name}_unz")
        Archive.unPackFromGZip(decode.absolutePath, unzip.name)

        val unsign =   File("${tempFolder.absolutePath}/${decodeFile.name}")

        Scad.unSign(unzip, unsign)

        unsign.copyTo(decodeFile, true)

        tempFolder.deleteFolder()

        return decodeFile
    }
}