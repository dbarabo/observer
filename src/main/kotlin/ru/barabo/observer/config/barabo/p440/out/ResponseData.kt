package ru.barabo.observer.config.barabo.p440.out

interface ResponseData {

    fun fileNameResponse(): String

    fun fileNameFromFns(): String

    fun fileNameResponseTemplate(): String

    fun idFromFns(): Number

    fun init(idResponse :Number): ResponseData
}