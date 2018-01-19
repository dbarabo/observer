package ru.barabo.observer.config.barabo.p440.out

import ru.barabo.db.SessionSetting

interface ResponseData {

    fun fileNameResponse(): String

    fun fileNameFromFns(): String

    fun fileNameResponseTemplate(): String

    fun idFromFns(): Number

    fun init(idResponse: Number, sessionSetting: SessionSetting): ResponseData

    fun typeInfo(): String

    fun xsdSchema(): String
}