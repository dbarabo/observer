package ru.barabo.xls

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import java.util.*
import kotlin.collections.ArrayList

enum class VarType(val sqlType: Int) {
    UNDEFINED(-1),
    INT(java.sql.Types.BIGINT),
    NUMBER(java.sql.Types.DOUBLE),
    VARCHAR(java.sql.Types.VARCHAR),
    DATE(java.sql.Types.TIMESTAMP),
    RECORD(-1),
    CURSOR(-1);

    fun copyValue(value: Any?): Any? {
        if(value == null) return null

        return when(this) {
        INT -> (value as Number).toLong()
        NUMBER -> (value as Number).toDouble()
        VARCHAR ->  (value as String) + ""
        DATE -> java.sql.Timestamp((value as Date).time)
        else -> throw Exception("it is not support set Action for $this value=$value")
        }
    }

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
           return VarType.values().firstOrNull { it.sqlType == sqlType } ?: varTypeBySqlTypeMore(sqlType)
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

private val logger = LoggerFactory.getLogger(Parser::class.java)

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

private class ColumnResult(private val cursor: CursorData, val columnName: String, private var index: Int = -1): ReturnResult {
    override fun getVar(): VarResult {
        if(index == -1) {
            index = cursor.getColumnIndex(columnName)
        }
        return cursor.getVarResult(index)
    }

    override fun setVar(newVar: VarResult) {}

    override fun getSqlValue(): Any {
        if(index == -1) {
            index = cursor.getColumnIndex(columnName)
        }
        return cursor.toSqlValue(index)
    }
}

class CursorData(val query: String, val params: List<ReturnResult> = emptyList() ) {

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

    fun toSqlValue(index: Int): Any {
        if(!isOpen) {
            isOpen = open()
        }
        if(row >= data.size) throw Exception("cursor position is end")

        return data[row][index] ?: sqlColumnType[index].toSqlValueNull()
    }

    fun getVarResult(index: Int): VarResult {
        if(!isOpen) {
            isOpen = open()
        }
        if(row >= data.size) throw Exception("cursor position is end")

        val value = data[row][index]

        logger.error("index=$index")
        logger.error("value=$value")
        logger.error("sqlColumnType=${sqlColumnType[index]}")

        val type = VarType.varTypeBySqlType(sqlColumnType[index])

        return VarResult(type = type, value = value)
    }

    internal fun getColumnIndex(columnName: String): Int {
        if(!isOpen) {
            isOpen = open()
        }

        return columns.withIndex().firstOrNull { it.value.equals(columnName, true) }?.index ?:
        throw Exception("not found column for cursor .$columnName")
    }

    private fun open(): Boolean {

        val param: Array<Any?>? = if(params.isEmpty()) null else params.map { it.getSqlValue() }.toTypedArray()

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

data class Var(var name: String, var value: VarResult) {

    fun toSqlValue(columnName: String? = null): Any = value.value?.let { toSqlValueIt(columnName, it) } ?: value.type.toSqlValueNull()

    @Suppress("UNCHECKED_CAST")
    private fun toSqlValueIt(columnName: String?, itValue: Any): Any {
        return when(value.type) {
            VarType.INT -> (itValue as Number).toLong()
            VarType.NUMBER -> itValue
            VarType.VARCHAR -> itValue
            VarType.DATE -> itValue
            VarType.RECORD -> getRecordValue(columnName, itValue as Record)
            //VarType.CURSOR -> (itValue as CursorData).toSqlValue(columnName)
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
    FUN
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
        return value?.let { it } ?: type.toSqlValueNull()
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
        val result = operations[oper]?.invoke(params, info) ?: throw Exception("operations for $oper not found")

        return result
    }
}

private val operations = mapOf<Oper, (List<VarResult>, String)->VarResult >(
    Oper.APPLY to ::apply,
    Oper.SQL_EXEC to ::sqlProcExec,
    Oper.FUN to ::funOper
)

private fun apply(params: List<VarResult>, info: String): VarResult {

    params[0].setVar(params[1].getVar())

    return params[0]
}

private fun sqlProcExec(params: List<VarResult>, info: String): VarResult {

  val outTypes =   params.filter { it.value == VarType.UNDEFINED}.map { it.type.sqlType }.toIntArray()

  val param: Array<Any?> = params.filter { it.value != VarType.UNDEFINED}.map {it.getVar().getSqlValue()}.toTypedArray()

  val outParams = AfinaQuery.execute(query = info, params = param, outParamTypes = outTypes)

  outParams?.withIndex()?.forEach {
      params[it.index].setVar(VarResult(params[it.index].type, it.value))
  }

  return VarResult.UNDEFINED
}

private fun funOper(params: List<VarResult>, info: String): VarResult =
        funMap[info.toUpperCase()]?.invoke(params) ?: throw Exception("fun for $info not found")

private val funMap = mapOf<String, (List<VarResult>)->VarResult> (
        "OUT" to ::outFun,
        "EQUAL" to ::equalFun,
        "NOTEQUAL" to ::notEqualFun,
        "NOT" to ::notFun,
        "AND" to ::andFun,
        "OR" to ::orFun
        //"NOW" to ::nowFun
)

//private fun nowFun(params: List<VarResult>): VarResult =

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
    VarType.NUMBER -> if((vars.value as? Number)?.toInt()?:0 == 0) 0 else 1

    VarType.VARCHAR -> if("".equals(vars.value)) 0 else 1

    VarType.DATE -> 1
    else -> throw Exception("do not to Int type ${vars.type}")
    }
}