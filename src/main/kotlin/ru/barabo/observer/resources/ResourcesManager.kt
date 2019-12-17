package ru.barabo.observer.resources

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.slf4j.LoggerFactory
import java.io.File

object ResourcesManager {
    val logger = LoggerFactory.getLogger(ResourcesManager::class.java)!!

    private val icoHash :HashMap<String, ImageView> = HashMap()

    private val ICO_PATH = "/ico/"

    fun icon(icoName :String) :ImageView {

        val ico = icoHash[icoName]

        if(ico != null) {
            return ico
        }

        val newIco =  loadIcon(icoName)

        logger.info("ResourcesManager newIco=$newIco")

        icoHash[icoName] = newIco

        return newIco
    }

    fun image(icoName :String) :Image = Image(pathResource(ICO_PATH + icoName))

    private fun loadIcon(icoName :String) :ImageView = ImageView(pathResource(ICO_PATH + icoName))

    private fun pathResource(fullPath :String) :String {
        val path = ResourcesManager::class.java.getResource(fullPath).toURI().toString()
        logger.info("ResourcesManager pathResource=$path")
        return path
    }

    fun copyFromJar(folderTo: File, resourcePath: String): File? {

        val nameRes = resourcePath.substringAfterLast("/")

        val fileOut = File("${folderTo.absolutePath}/$nameRes")

        val stream = javaClass.getResourceAsStream(resourcePath)

        stream?.use { input ->
            fileOut.outputStream().use { fileOut ->
                input.copyTo(fileOut)
            }
        }

        return stream?.let { fileOut }
    }
 }