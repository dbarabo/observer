package ru.barabo.db.param

interface DbParam<T> {

    fun type() :Int

    var value :T?

    fun convert(newValue :Any) :T?

    fun setVal(newValue :Any?) {
        value = newValue?.let { convert(newValue) }?: null
    }

}