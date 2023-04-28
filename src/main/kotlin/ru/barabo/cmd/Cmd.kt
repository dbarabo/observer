package ru.barabo.cmd

import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

object Cmd {

    private val logger = LoggerFactory.getLogger(Cmd::class.java)

    @Throws(IOException::class, InterruptedException::class)
    fun processBuild(program: String, param: String): List<String> {

        val args = param.split(" ")

        val argsWithCommand = ArrayList<String>()
        argsWithCommand += program
        argsWithCommand.addAll(args)

        var count = 0
        var result: Int

        var lines: List<String>

        do {
            val process = ProcessBuilder(argsWithCommand).redirectErrorStream(true).start()

            lines = process.inputStream.reader(Charset.forName("CP866")).readLines()

            result = process.waitFor()

            count++

            if(result != 0) {
                Thread.sleep(1000)
            }
        } while ((result != 0) && count < 6)

        if(result != 0) throw IOException("return code=$result count = $count for process=$program $param")

        return lines
    }

    @Throws(IOException::class, InterruptedException::class)
    fun execCmd(cmd :String, checkErrorStream: (String)->Unit = {}) {

        try {
            logger.error("cmd=$cmd")

            val start = System.nanoTime()

            val process = Runtime.getRuntime().exec(cmd)

            StreamReader(process.errorStream) {
                logger.error("ERR_LOG:$it")

                checkErrorStream(it)
            }.start()
            StreamReader(process.inputStream) {
                logger.error("INPUT_LOG:$it")

                checkErrorStream(it)
            }.start()

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
            StreamReader(process.inputStream, logger::error).start()

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

    private val TEMP_FOLDER = "$JAR_FOLDER/temp" // "F:/Observer/temp"

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

