package ru.barabo.db

import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

interface ConverterValue {

    fun convertToBase(value :Any) :Any {
        LoggerFactory.getLogger(ConverterValue::class.java).info("value=$value")

        LoggerFactory.getLogger(ConverterValue::class.java).info("class=${value.javaClass.canonicalName}")

        return when (value) {
            is Enum<*>  -> value.ordinal
            is LocalDateTime -> Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
            else -> value.javaClass.canonicalName
        }
    }

    fun convertFromBase(value :Any, javaType :Class<*>) :Any?
}