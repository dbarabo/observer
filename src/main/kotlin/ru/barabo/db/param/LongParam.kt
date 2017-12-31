package ru.barabo.db.param

class LongParam(override var value: Long?) :DbParam<Long> {

    override fun type(): Int = java.sql.Types.BIGINT

    override fun convert(newValue: Any): Long? {
        if(newValue !is Number) throw ClassCastException("$newValue is not Number type")

        return newValue.toLong()
    }
}