package ru.barabo.archive

import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.cmd.deleteFolder
import ru.barabo.observer.config.task.finder.isFind
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.*
import java.util.*
import java.util.regex.Pattern


object Archive {

    private val logger = LoggerFactory.getLogger(Archive::class.java)

    private fun rarExtract(rarFile :File) = "c:/progra~1/winrar/winrar e ${rarFile.absolutePath} ${rarFile.parent}"

    @Throws(IOException::class)
    fun extractFromCab(cabFile :File, toFolder :String, regExp :String = ".*") :Array<File>? =
            extractFromArchive(cabFile, toFolder, regExp, "cab")

    @Throws(IOException::class)
    fun extractFromArj(arjFile :File, toFolder :String, regExp :String = ".*") :Array<File>? =
            extractFromArchive(arjFile, toFolder, regExp, "arj")

    @Throws(IOException::class)
    fun extractFromZip(zipFile :File, toFolder :String, regExp :String = ".*") :Array<File>? =
            extractFromArchive(zipFile, toFolder, regExp, "zip")

    @Throws(IOException::class)
    private fun extractFromArchive(cabFile :File, toFolder :String, regExp :String, extArchive :String) :Array<File>? {

        val tempCab = tempArchive(extArchive)

        val tempFolder = File(tempCab.parent)

        val search = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val newFiles: Array<File>?

        try {
            cabFile.copyTo(tempCab, true)

            Cmd.execCmd( rarExtract(tempCab) )

            tempCab.delete()

            val files: Array<File>? = tempFolder.listFiles { f ->  search.isFind(f.name, false)}

            newFiles = if(files == null) null else  files.copyOf()

            files?.indices?.forEach { newFiles!![it] = files[it].copyTo(File(toFolder + "/" + files[it].name), overwrite = true ) }

            tempFolder.deleteFolder()
        } catch (e :IOException) {
            logger.error("extractFromCab", e)

            tempFolder.deleteFolder()

            throw IOException(e.message)
        }

        return newFiles
    }

    private fun addToArj(archivePath :String, filePath :String)  = "arj32.exe a -e $archivePath $filePath"

    fun addToArj(archiveFullPath :String, files :Array<File>) {

        files.forEach {
            Cmd.execCmd( addToArj(archiveFullPath, it.absolutePath) )
        }
    }

    private fun tempFolder() :File = Cmd.tempFolder("a")

    private fun tempArchive(ext :String = "cab") :File = File("${tempFolder().absolutePath}/temp.$ext")

    fun createZip(zipFile :File, vararg files :File) :File {

        val zipSystem = createZipFileSystem(zipFile.absolutePath)

        val root = zipSystem.getPath("/").toString()

        files.forEach {
            val source = Paths.get(it.absolutePath)

            val dest = zipSystem.getPath(root, it.name)

            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING)
        }

        return zipFile
    }

    @Throws(IOException::class)
    private fun createZipFileSystem(zipFilename: String, create: Boolean = true): FileSystem {

        val uri = URI.create("jar:file:${Paths.get(zipFilename).toUri().path}")

        val env = HashMap<String, String>()
        if (create) {
            env.put("create", "true")
        }
        return FileSystems.newFileSystem(uri, env)
    }
}
