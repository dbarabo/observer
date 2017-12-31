package ru.barabo.db.param

class IntParam(override var value: Int?) :DbParam<Int> {
    override fun type(): Int = java.sql.Types.INTEGER

    override fun convert(newValue: Any): Int? {
        if(newValue !is Number) throw ClassCastException("$newValue is not Number type")

        return newValue.toInt()
    }
}