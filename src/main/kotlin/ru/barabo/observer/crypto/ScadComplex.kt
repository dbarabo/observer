package ru.barabo.observer.crypto

import ru.barabo.archive.Archive
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.crypto.task.nameDateToday
import java.io.File

object ScadComplex {

    fun unsignAndMoveSource(signFile: File, moveSourceFolder: File? = null): File {

        val tempFolder = Cmd.tempFolder("unsgn")
        val copySign = File("${tempFolder.absolutePath}/${signFile.name}")
        signFile.copyTo(copySign, true)

        val unsign = File("${tempFolder.absolutePath}/${signFile.name}_unsgn")
        Scad.unSign(copySign, unsign)

        if(moveSourceFolder != null) {
            val newSource = File("${moveSourceFolder.absolutePath}/${signFile.name}")
            signFile.copyTo(newSource, true)
            signFile.delete()
        }
        unsign.copyTo(signFile, true)
        tempFolder.deleteFolder()

        return signFile
    }

    fun signAndMoveSource(sourceFile: File, moveSourceFolder: File? = null): File {

        val newSourceFolder = moveSourceFolder ?: sourceFile.parentFile?.parentFile ?: throw Exception("parent not found")

        newSourceFolder.absolutePath.byFolderExists()

        val tempFolder = Cmd.tempFolder("sgn")
        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val sign = File("${tempFolder.absolutePath}/${sourceFile.name}_sgn")
        Scad.sign(copySource, sign)

        val newSource = File("${newSourceFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(newSource, true)
        sourceFile.delete()

        sign.copyTo(sourceFile, true)

        tempFolder.deleteFolder()

        return sourceFile
    }

    fun crypto4077U(sourceFile: File): File {
        val tempFolder = Cmd.tempFolder("u4077")

        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val sign = File("${tempFolder.absolutePath}/${sourceFile.name}_sgn")
        Scad.sign(copySource, sign)

        val signGzip = File("${tempFolder.absolutePath}/${sourceFile.name}_sgz")

        Archive.packToGZip(signGzip.absolutePath, sign)

        copySource.delete()

        val encode = copySource

        Scad.encode(signGzip, encode, CertificateType.FSFM)

        val arjArchive = File("${tempFolder.absolutePath}/${arjArchiveNameToday()}")
        Archive.addToArj(arjArchive.absolutePath, arrayOf(encode) )

        val signArj = File("${tempFolder.absolutePath}/${arjArchive.name}_sgz")
        Scad.sign(arjArchive, signArj)

        val destArchive = File("${sourceFile.parent}/${arjArchive.name}")

        signArj.copyTo(destArchive, true)

        tempFolder.deleteFolder()

        return destArchive
    }

    private fun arjArchiveNameToday() = "FM4077U_040507717_${nameDateToday()}_001.ARJ"

    fun cryptoOnlyFtsVal(sourceFile: File, encodeFile: File, certType: CertificateType, isDelSource: Boolean = true): File {

        val file = gzipCrypto(sourceFile, encodeFile, certType, certType.name.substring(0..3))

        if(!file.exists()) throw Exception("file not found ${encodeFile.absolutePath}")

        if(isDelSource) {
            sourceFile.delete()
        }

        return encodeFile
    }

    fun fullEncode311p(sourceFile: File, encodeFile: File, isAddFss: Boolean = true): File
            = signGzipEncodeFns(sourceFile, encodeFile, isAddFss, "p311")

    fun fullEncode440p(sourceFile: File, encodeFile: File): File = signGzipEncodeFns(sourceFile, encodeFile, false, "p440")

    fun fullDecode440p(sourceFile: File, decodeFile: File): File = decodeUngzipUnsign(sourceFile, decodeFile, "p440")

    fun fullDecode364p(sourceFile: File, decodeFile: File): File = decodeUngzipUnsign(sourceFile, decodeFile, "p364")

    fun decodeAny(sourceFile: File, decodeFile: File? = null): File = decodeUngzip(sourceFile, decodeFile, "any")

    private fun gzipCrypto(sourceFile: File, encodeFile: File, certType: CertificateType, prefix: String): File {
        val tempFolder = Cmd.tempFolder(prefix)

        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val gzip = File("${tempFolder.absolutePath}/${sourceFile.name}_gz")

        Archive.packToGZip(gzip.absolutePath, copySource)
        copySource.delete()

        val encode = File("${tempFolder.absolutePath}/${encodeFile.name}")

        Scad.encode(gzip, encode, certType)

        encode.copyTo(encodeFile, true)

        tempFolder.deleteFolder()

        return encodeFile
    }

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

        val unsign = File("${tempFolder.absolutePath}/${decodeFile.name}")

        Scad.unSign(unzip, unsign)

        unsign.copyTo(decodeFile, true)

        tempFolder.deleteFolder()

        return decodeFile
    }

    private fun decodeUngzip(sourceFile: File, decodeFile: File? = null, prefix: String): File {
        val tempFolder = Cmd.tempFolder(prefix)

        val copySource = File("${tempFolder.absolutePath}/${sourceFile.name}")
        sourceFile.copyTo(copySource, true)

        val decode = File("${tempFolder.absolutePath}/${sourceFile.name}_dec")
        Scad.decode(copySource, decode)

        val unzip = File("${tempFolder.absolutePath}/${sourceFile.name}_unz")
        Archive.unPackFromGZip(decode.absolutePath, unzip.name)

        val newDecode = decodeFile ?: sourceFile

        unzip.copyTo(newDecode, true)

        tempFolder.deleteFolder()

        return newDecode
    }
}