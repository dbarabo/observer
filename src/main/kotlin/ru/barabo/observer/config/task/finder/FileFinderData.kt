package ru.barabo.observer.config.task.finder

import java.io.File
import java.util.regex.Pattern

data class FileFinderData (val directory :()->File,
                           val search : Pattern? = null,
                           val isNegative :Boolean = false,
                           val isModifiedTodayOnly :Boolean = false) {

    public constructor(pathFolder :String, regExp :String, isNegative :Boolean = false, isModifiedTodayOnly :Boolean = false)
            : this({File(pathFolder)}, Pattern.compile(regExp, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE),
            isNegative, isModifiedTodayOnly  )


    public constructor(directory :()->File, regExp :String, isNegative :Boolean = false, isModifiedTodayOnly :Boolean = false)
            : this(directory, Pattern.compile(regExp, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE),
            isNegative, isModifiedTodayOnly  )
}