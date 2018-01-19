package ru.barabo.cmd

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class StreamReader(private val inputStream : InputStream,
                   private val processLine :(String)->Unit) : Thread() {

    override fun run() {
        val reader = BufferedReader(InputStreamReader(inputStream))

        do {
            val line = reader.readLine() ?: break

            processLine(line)
        }while(true)

        try {
            reader.close()
        } catch (e : Exception) {}
    }
}