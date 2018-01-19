package ru.barabo.cmd

import java.io.File
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory


object XmlValidator {

    fun validate(xmlFile: File, xsdInJar: String?) {

        val xmlStream = StreamSource(xmlFile)

        val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)

        val urlXsd = XmlValidator::class.java.javaClass.getResource(xsdInJar)

        val schema = schemaFactory.newSchema(urlXsd)

        val validator = schema!!.newValidator()

        validator.validate(xmlStream)
    }
}