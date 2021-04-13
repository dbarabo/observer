package ru.barabo.observer.crypto

import org.jasypt.util.text.BasicTextEncryptor
import ru.barabo.cmd.Cmd
import java.io.File

object MasterKey {

    private lateinit var textEncryptor: BasicTextEncryptor

    private const val MASTER_FILENAME = "masterkey"

    private const val SEPARATOR_KEY = '='

    private val MASTER_FILE = File("${Cmd.LIB_FOLDER}/$MASTER_FILENAME")

    private var key: String = ""

    private val data = HashMap<String, String>()

   //private val logger = LoggerFactory.getLogger(MasterKey::class.java)

    init {

        loadKeys()
    }

    fun loadKeys() {

        val masterFile = if(MASTER_FILE.exists()) MASTER_FILE else findMasterLib()!!

        if(!masterFile.exists()) return

        val lines = masterFile.readLines(Charsets.UTF_8)

        if(lines.isEmpty()) return

        setMasterKey(lines[0])

        data.clear()

        lines.drop(0).forEach { line ->
            val pos = line.indexOf(SEPARATOR_KEY) - 1

            if(pos >= 0) {
                val key =  line.substring(0..pos).trim()

                val value = line.substring(pos + 2)

                data[key] = decrypt(value)
            }
        }
    }

    private fun findMasterLib(): File? {

        var dirLib = MASTER_FILE

        do {
            dirLib = dirLib.parentFile

            val libDir = File("${dirLib.path}/lib")

            if(libDir.exists() ) {
                return File("${libDir.path}/$MASTER_FILENAME")
            }
        } while (!libDir.exists())

        return null
    }

    fun value(key: String): String = data[key]!!

    fun getValue(key: String): String? = data[key]

    fun putKeyValue(key: String, value: String) {
        data[key] = value
    }

    fun clearAll() {
        data.clear()
    }

    fun saveKeys() {

        val text = "$key\n" + data.entries.joinToString("\n") { "${it.key}=${toCrypto(it.value)}" }

        MASTER_FILE.writeText(text, Charsets.UTF_8)
    }

    fun setMasterKey(masterKey: String?) {
        if(masterKey.isNullOrEmpty()) {
            key = ""
        } else {
            key = masterKey

            textEncryptor = BasicTextEncryptor()

            textEncryptor.setPassword(key)
        }
    }

    private fun toCrypto(value: String): String = if(key.isEmpty()) value else textEncryptor.encrypt(value)

    private fun decrypt(encryptText: String) = if(key.isEmpty()) encryptText else textEncryptor.decrypt(encryptText)
}