package ru.barabo.db.param

import java.time.LocalDateTime
import java.time.ZoneId

class DateTimeParam(override var value: LocalDateTime?) :DbParam<LocalDateTime> {
    override fun type(): Int = java.sql.Types.TIMESTAMP

    override fun convert(newValue: Any): LocalDateTime? {
        if(newValue !is java.util.Date) throw ClassCastException("$newValue is not java.util.Date type")

        return (newValue as java.util.Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

}