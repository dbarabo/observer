package ru.barabo.observer.config.task

import java.time.Duration
import java.time.LocalTime

data class AccessibleData (val workWeek : WeekAccess = WeekAccess.WORK_ONLY,
                           val isDuplicateName :Boolean = false,
                           val workTimeFrom :LocalTime =  LocalTime.MIN,
                           val workTimeTo :LocalTime =  LocalTime.MAX,
                           val executeWait :Duration? = Duration.ZERO)