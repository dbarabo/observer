package ru.barabo.xls

import ru.barabo.observer.afina.AfinaQuery
import java.lang.Exception

enum class VarType {
    REF,
    NUMBER,
    VARCHAR,
    DATE,
    RECORD,
    CURSOR;

    fun toSqlValueNull(): Any {
        return when(this) {
            REF -> Long::class.javaObjectType

            NUMBER -> Double::class.javaObjectType

            VARCHAR -> ""

            DATE -> java.time.LocalDateTime::class.javaObjectType

            else -> throw Exception("undefined type for null value")
        }
    }
}

fun Int.toSqlValueNull(): Any {
    return when(this) {
        java.sql.Types.INTEGER,
        java.sql.Types.SMALLINT,
        java.sql.Types.BIT,
        java.sql.Types.BIGINT -> Long::class.javaObjectType

        java.sql.Types.FLOAT,
        java.sql.Types.REAL,
        java.sql.Types.DOUBLE,
        java.sql.Types.NUMERIC,
        java.sql.Types.DECIMAL -> Double::class.javaObjectType

        java.sql.Types.CHAR,
        java.sql.Types.VARCHAR -> ""

        java.sql.Types.DATE,
        java.sql.Types.TIME,
        java.sql.Types.TIMESTAMP ->   java.time.LocalDateTime::class.javaObjectType

        else -> throw Exception("undefined type for null value")
    }
}

data class Var(var name: String, var type: VarType, var value: Any?) {

    fun toSqlValue(columnName: String? = null): Any = value?.let { toSqlValueIt(columnName, it) } ?: type.toSqlValueNull()

    @Suppress("UNCHECKED_CAST")
    private fun toSqlValueIt(columnName: String?, itValue: Any): Any {
        return when(type) {
            VarType.REF -> (itValue as Number).toLong()
            VarType.NUMBER -> itValue
            VarType.VARCHAR -> itValue
            VarType.DATE -> itValue
            VarType.RECORD -> getRecordValue(columnName, itValue as Record)
            VarType.CURSOR -> (itValue as CursorData).toSqlValue(columnName)
        }
    }

    private fun getRecordValue(columnName: String?, value: Record): Any {

        val index = if(columnName == null) 0 else columnName.trim().toIntOrNull()

        return index?.let { value[it].toSqlValue() } ?:
            value.firstOrNull { it.name.equals(columnName, true) } ?:
            throw Exception("not found record $name.$columnName")
    }
}

data class CursorData(val query: String, val params: List<Var> = emptyList(),
                      var data: List<Array<Any?>> = emptyList(), var row: Int = 0,
                      var columns: List<String> = emptyList(), var sqlColumnType: List<Int> = emptyList(),
                      private var isOpen: Boolean = false) {

    fun toSqlValue(columnName: String?): Any {

        if(!isOpen) {
            isOpen = open()
        }

        if(row >= data.size) throw Exception("cursor position is end")

        val index = if(columnName == null) 0 else {
            columnName.trim().toIntOrNull() ?: getColumnIndex(columnName.trim())
        }

        return data[row][index] ?: sqlColumnType[index].toSqlValueNull()
    }

    private fun getColumnIndex(columnName: String): Int {
        return columns.withIndex().firstOrNull { it.value.equals(columnName, true) }?.index ?:
        throw Exception("not found column for cursor .$columnName")
    }

    private fun open(): Boolean {

        val param: Array<Any?>? = if(params.isEmpty()) null else params.toSqlParams()

        val allData = if(isCursor()) {
            AfinaQuery.selectCursorWithMetaData(query, param)
        } else {
            AfinaQuery.selectWithMetaData(query, param)
        }

        data = allData.data
        columns = allData.columns
        sqlColumnType = allData.types

        return true
    }

    private fun isCursor() = query[0] == '{'
}

typealias Record = Array<Var>

private fun List<Var>.toSqlParams(): Array<Any?> = map { it.toSqlValue() }.toTypedArray()
