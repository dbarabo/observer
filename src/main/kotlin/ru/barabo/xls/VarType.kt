package ru.barabo.xls

import ru.barabo.db.Query
import ru.barabo.db.SessionSetting
import java.util.*
import kotlin.collections.ArrayList

enum class VarType(val sqlType: Int) {
    UNDEFINED(-1),
    INT(java.sql.Types.BIGINT),
    NUMBER(java.sql.Types.DOUBLE),
    VARCHAR(java.sql.Types.VARCHAR),
    DATE(java.sql.Types.TIMESTAMP),
    RECORD(-1),
    CURSOR(-1),
    SQL_PROC(-1);

    fun toSqlValueNull(): Any {
        return when(this) {
            INT -> Long::class.javaObjectType

            NUMBER -> Double::class.javaObjectType

            VARCHAR -> ""

            DATE -> java.time.LocalDateTime::class.javaObjectType

            else -> throw Exception("undefined type for null value")
        }
    }

    companion object {
        fun varTypeBySqlType(sqlType: Int): VarType {
           return values().firstOrNull { it.sqlType == sqlType } ?: varTypeBySqlTypeMore(sqlType)
        }

        private fun varTypeBySqlTypeMore(sqlType: Int): VarType {
            return when(sqlType) {
            java.sql.Types.BIT,
            java.sql.Types.TINYINT,
            java.sql.Types.SMALLINT,
            java.sql.Types.INTEGER -> INT

            java.sql.Types.NUMERIC,
            java.sql.Types.DECIMAL,
            java.sql.Types.FLOAT,
            java.sql.Types.REAL -> NUMBER
            else -> throw java.lang.Exception("not supported sqlType=$sqlType")
            }
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

data class Record(var columns: List<Var> = emptyList())

private class ColumnResult(private val cursor: CursorData, val columnName: String,
                           private var index: Int = UNINITIALIZE_COLUMN_INDEX,
                           private var funIndex: Int = UNINITIALIZE_COLUMN_INDEX): ReturnResult {
    override fun getVar(): VarResult {
        checkInitIndexes()

        return cursor.getVarResult(index, funIndex)
    }

    override fun setVar(newVar: VarResult) {}

    override fun getSqlValue(): Any {
        checkInitIndexes()

        return cursor.toSqlValue(index, funIndex)
    }

    private fun checkInitIndexes() {
        if(index == UNINITIALIZE_COLUMN_INDEX) {
            val (newIndex, newFun) =  cursor.getColumnIndex(columnName)
            index = newIndex
            funIndex = newFun
        }
    }
}

private const val UNINITIALIZE_COLUMN_INDEX = Int.MAX_VALUE

data class QuerySession(val query: Query, val sessionSetting: SessionSetting)

class CursorData(private val querySession: QuerySession, private val querySelect: String,
                 val params: List<ReturnResult> = emptyList() ) {

    private var data: List<Array<Any?>> = emptyList()

    private var row: Int = 0

    private var columns: List<String> = emptyList()

    private var sqlColumnType: List<Int> = emptyList()

    private var isOpen: Boolean = false

    private val columnResult = ArrayList<ColumnResult>()

    fun getColumnResult(columnName: String): ReturnResult {
        return columnResult.firstOrNull { it.columnName == columnName }
                ?: ColumnResult(this, columnName).apply { columnResult += this }
    }

    fun isNext(): Boolean {
        return if(row + 1 < data.size) {
            row++

            true
        } else false
    }

    fun isEmpty(): Boolean {
        if(!isOpen) {
            isOpen = open()
        }
        return data.isEmpty()
    }

    fun toSqlValue(index: Int, funIndex: Int): Any {
        if(!isOpen) {
            isOpen = open()
        }

        if(index < 0 || funIndex != UNINITIALIZE_COLUMN_INDEX) {
            return funCursorValue(index, funIndex)
        }

        if(row >= data.size) throw Exception("cursor position is end")

        return data[row][index] ?: sqlColumnType[index].toSqlValueNull()
    }

    fun getVarResult(index: Int, funIndex: Int): VarResult {
        if(!isOpen) {
            isOpen = open()
        }

        if(index < 0 || funIndex != UNINITIALIZE_COLUMN_INDEX) {
            return VarResult(type = VarType.NUMBER, value = funCursorValue(index, funIndex))
        }

        if(row >= data.size) throw Exception("cursor position is end")

        val value = data[row][index]

        val type = VarType.varTypeBySqlType(sqlColumnType[index])

        return VarResult(type = type, value = value)
    }

    private fun funCursorValue(index: Int, funIndex: Int): Any {

        val findIndex = if(index < 0) index else funIndex

        val cursorFun = CursorFun.byIndex(findIndex) ?: throw Exception("cursor fun is not found index=$index")

        return cursorFun.func.invoke(this, index)
    }

    internal fun getColumnIndex(columnName: String): Pair<Int, Int> {
        if(!isOpen) {
            isOpen = open()
        }

        val column = columnName.substringBefore('.')

        val funColumn = columnName.substringAfter('.', "")

        val columnIndex = columnIndexByName(column)

        val funIndex = if(funColumn.isBlank()) UNINITIALIZE_COLUMN_INDEX else columnIndexByName(funColumn)

        return Pair(columnIndex, funIndex)
    }

    private fun columnIndexByName(columnName: String): Int {
        return CursorFun.byColumn(columnName)?.index ?:
        columns.withIndex().firstOrNull { it.value.equals(columnName, true) }?.index ?:
        throw Exception("not found column for cursor .$columnName")
    }

    private fun open(): Boolean {

        val param: Array<Any?>? = if(params.isEmpty()) null else params.map { it.getSqlValue() }.toTypedArray()

        val allData = if(isCursor()) {
            querySession.query.selectCursorWithMetaData(querySelect, param, querySession.sessionSetting)
        } else {
            querySession.query.selectWithMetaData(querySelect, param, querySession.sessionSetting)
        }

        data = allData.data
        columns = allData.columns
        sqlColumnType = allData.types
        row = 0

        return true
    }

    private fun isCursor() = querySelect[0] == '{'

    fun row(@Suppress("UNUSED_PARAMETER") columnIndex: Int): Any = row + 1

    fun sum(columnIndex: Int): Any = data.sumOf { (it[columnIndex] as? Number)?.toDouble() ?: 0.0 }

    fun max(columnIndex: Int): Any = data.map { (it[columnIndex] as? Number)?.toDouble()?:0.0 }.maxOrNull() ?:0.0

    fun min(columnIndex: Int): Any = data.map { (it[columnIndex] as? Number)?.toDouble()?:0.0 }.minOrNull() ?:0.0

    fun count(@Suppress("UNUSED_PARAMETER") columnIndex: Int): Any = data.size

    fun isEmptyFun(@Suppress("UNUSED_PARAMETER") columnIndex: Int): Any = if(isEmpty()) 1 else 0

    fun isNotEmptyFun(@Suppress("UNUSED_PARAMETER") columnIndex: Int): Any = if(isEmpty()) 0 else 1
}

enum class CursorFun(val index: Int, val funName: String, val func: CursorData.(columnIndex: Int)->Any ) {
    ROW(-1, "ROW", CursorData::row),
    SUM(-2, "SUM", CursorData::sum),
    COUNT(-3, "COUNT", CursorData::count),
    ISEMPTY(-4, "ISEMPTY", CursorData::isEmptyFun),
    ISNOTEMPTY(-5, "ISNOTEMPTY", CursorData::isNotEmptyFun),
    MIN(-6, "MIN", CursorData::min),
    MAX(-7, "MAX", CursorData::max);

    companion object {
        fun byIndex(index: Int): CursorFun? = values().firstOrNull { it.index == index }

        fun byColumn(funName: String): CursorFun? = values().firstOrNull { it.funName == funName.uppercase(Locale.getDefault()) }
    }
}

data class Var(var name: String, var value: VarResult) {

    private fun toSqlValue(columnName: String? = null): Any =
            value.value?.let { toSqlValueIt(columnName, it) } ?: value.type.toSqlValueNull()

    @Suppress("UNCHECKED_CAST")
    private fun toSqlValueIt(columnName: String?, itValue: Any): Any {
        return when(value.type) {
            VarType.INT -> (itValue as Number).toLong()
            VarType.NUMBER -> itValue
            VarType.VARCHAR -> itValue
            VarType.DATE -> itValue
            VarType.RECORD -> getRecordValue(columnName, itValue as Record)
            else -> throw Exception("undefined value for $name.$columnName")
        }
    }

    private fun getRecordValue(columnName: String?, value: Record): Any {

        val index = if(columnName == null) 0 else columnName.trim().toIntOrNull()

        return index?.let { value.columns[it].toSqlValue() } ?:
        value.columns.firstOrNull { it.name.equals(columnName, true) } ?:
        throw Exception("not found record $name.$columnName")
    }
}

enum class Oper {
    APPLY,
    SQL_EXEC,
    FUN,
    VAR
}

interface ReturnResult {
    fun getVar(): VarResult

    fun setVar(newVar: VarResult)

    fun getSqlValue(): Any
}

data class VarResult(var type: VarType = VarType.UNDEFINED, var value: Any? = null) : ReturnResult {

    companion object {
        val UNDEFINED = VarResult()
    }

    override fun getVar(): VarResult = this

    override fun setVar(newVar: VarResult) {
        this.type = newVar.type
        this.value = newVar.value // newVar.type.copyValue(newVar.value)
    }

    override fun getSqlValue(): Any {
        return value ?: type.toSqlValueNull()
    }
}

fun VarResult?.toBoolean(): Boolean {
    if(this?.value == null) return false

    return when(this.type) {
        VarType.UNDEFINED -> false
        VarType.INT -> (this.value as Number).toLong() != 0L
        VarType.NUMBER -> (this.value as Number).toDouble() != 0.0
        VarType.VARCHAR -> this.value.toString().isBlank()
        VarType.DATE -> true
        else -> false
    }
}

data class OperVar(val oper: Oper,
                   val info: String = "",
                   val vars: List<ReturnResult> = emptyList()  ) : ReturnResult {

    override fun getVar(): VarResult {
        val params = vars.map { it.getVar() }

        return oper(params)
    }

    override fun setVar(newVar: VarResult) {}

    override fun getSqlValue(): Any = getVar().getSqlValue()

    private fun oper(params: List<VarResult>): VarResult {

        return operations[oper]?.invoke(params, info) ?: throw Exception("operations for $oper not found")
    }
}

private val operations = mapOf<Oper, (List<VarResult>, String)->VarResult >(
    Oper.APPLY to ::apply,
    Oper.SQL_EXEC to ::sqlProcExec,
    Oper.FUN to ::funOper,
    Oper.VAR to ::varOper
)

private fun varOper(params: List<VarResult>, @Suppress("UNUSED_PARAMETER") info: String): VarResult {
    return params[0]
}

private fun apply(params: List<VarResult>, @Suppress("UNUSED_PARAMETER") info: String): VarResult {

    params[0].setVar(params[1].getVar())

    return params[0]
}

private fun sqlProcExec(params: List<VarResult>, info: String): VarResult {

    val querySession: QuerySession = params[0].value as? QuerySession ?: throw Exception("first param for sqlProcExec must be QuerySession")

    val outTypes =   params.filter { it.value == VarType.UNDEFINED}.map { it.type.sqlType }.toIntArray()

    val param: Array<Any?> = params.filter { it.value != VarType.UNDEFINED && it.type != VarType.SQL_PROC}.map {it.getVar().getSqlValue()}.toTypedArray()

    val outParams = querySession.query.execute(query = info, params = param,
          outParamTypes = outTypes, sessionSetting = querySession.sessionSetting)

    outParams?.withIndex()?.forEach {
        params[it.index + 1].setVar(VarResult(params[it.index + 1].type, it.value))
    }

  return VarResult.UNDEFINED
}

private fun funOper(params: List<VarResult>, info: String): VarResult =
    funMap[info.uppercase(Locale.getDefault())]?.invoke(params) ?: throw Exception("fun for $info not found")

private val funMap = mapOf<String, (List<VarResult>)->VarResult> (
        "OUT" to ::outFun,
        "EQUAL" to ::equalFun,
        "NOTEQUAL" to ::notEqualFun,
        "NOT" to ::notFun,
        "AND" to ::andFun,
        "OR" to ::orFun
)

private fun outFun(params: List<VarResult>): VarResult = params[0].apply { this.value = VarType.UNDEFINED }

private fun notEqualFun(params: List<VarResult>): VarResult = VarResult( VarType.INT, if(params[0].value != params[1].value)1 else 0)

private fun equalFun(params: List<VarResult>): VarResult = VarResult( VarType.INT, if(params[0].value == params[1].value)1 else 0)

private fun notFun(params: List<VarResult>): VarResult = VarResult( VarType.INT, if(toSign(params[0]) == 0) 1 else 0)

private fun andFun(params: List<VarResult>): VarResult = VarResult( VarType.INT, toSign(params[0]) and toSign(params[1]))

private fun orFun(params: List<VarResult>): VarResult = VarResult( VarType.INT, toSign(params[0]) or toSign(params[1]))

private fun toSign(vars: VarResult): Int {

    if(vars.value == null) return 0

    return when(vars.type) {
    VarType.INT,
    VarType.NUMBER -> if(((vars.value as? Number)?.toInt() ?: 0) == 0) 0 else 1

    VarType.VARCHAR -> if("" == vars.value) 0 else 1

    VarType.DATE -> 1
    else -> throw Exception("do not to Int type ${vars.type}")
    }
}