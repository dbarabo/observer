package ru.barabo.db.param

class StringParam(override var value: String?) :DbParam<String> {
    override fun type(): Int = java.sql.Types.VARCHAR

    override fun convert(newValue: Any): String? = newValue.toString()
}