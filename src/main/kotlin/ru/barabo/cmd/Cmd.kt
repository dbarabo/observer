package ru.barabo.cmd

import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object Cmd {

    private val logger = LoggerFactory.getLogger(Cmd::class.java)

    @Throws(IOException::class, InterruptedException::class)
    fun execCmd(cmd :String) {

        try {
            val start = System.nanoTime()

            val process = Runtime.getRuntime().exec(cmd)

            StreamReader(process.errorStream, logger::error).start()
            StreamReader(process.inputStream, logger::error).start()

            process.waitFor(MAX_WAIT_MINUTES, TimeUnit.MINUTES)

            val temporal = System.nanoTime() - start

            if(temporal >= TimeUnit.MINUTES.toNanos(MAX_WAIT_MINUTES) ) {
                logger.error("execCmd cmd=$cmd waitNanos=$temporal")

                throw IOException("TimeOut execCmd cmd=$cmd waitNanos=$temporal")
            }
        } catch (e :Exception) {

            logger.error("execCmd", e)
            throw Exception(e.message)
        }
    }

    private const val MAX_WAIT_MINUTES = 20L


    fun execDos(cmdDos: String) {
        try {
            val start = System.nanoTime()

            val execCmd = arrayOf("cmd.exe", "/C", cmdDos)

            val process = Runtime.getRuntime().exec(execCmd)

            StreamReader(process.errorStream, logger::error).start()
            StreamReader(process.inputStream, logger::info).start()

            process.waitFor(MAX_WAIT_MINUTES, TimeUnit.MINUTES)

            val temporal = System.nanoTime() - start

            if(temporal >= TimeUnit.MINUTES.toNanos(MAX_WAIT_MINUTES) ) {
                logger.error("execDos cmd=$cmdDos waitNanos=$temporal")

                throw IOException("TimeOut execDos cmd=$cmdDos waitNanos=$temporal")
            }
        } catch (e :Exception) {

            logger.error("execCmd", e)
            throw Exception(e.message)
        }
    }

    private fun cTemp(prefix: String) =  "$TEMP_FOLDER/$prefix${Date().time}" // "c:/temp/$prefix${Date().time}"

    private fun String.cyrReplace() =  this.replace("[^A-Za-z0-9] ".toRegex(), "F")

    fun tempFolder(prefix: String = ""): File {
        val temp = File(cTemp(prefix.cyrReplace()) )

        temp.mkdirs()

        return temp
    }

    val JAR_FOLDER = File(Cmd::class.java.protectionDomain.codeSource.location.path).parentFile.path

    val LIB_FOLDER = "$JAR_FOLDER/lib"

    val LOG_FOLDER = "$JAR_FOLDER/log"

    val CERT_FOLDER = "$JAR_FOLDER\\cert"

    private val TEMP_FOLDER = "$JAR_FOLDER/temp"

    fun createFolder(folderPath: String): File {

        val folder = File(folderPath)

        if(!folder.exists()) {
            folder.mkdirs()
        }

        return folder
    }
}

fun File.deleteFolder() {
    this.listFiles()?.forEach { it.delete() }

    this.delete()
}

