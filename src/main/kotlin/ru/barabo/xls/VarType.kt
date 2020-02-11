package ru.barabo.xls

import ru.barabo.observer.afina.AfinaQuery

enum class VarType(val sqlType: Int) {
    UNDEFINED(-1),
    INT(java.sql.Types.BIGINT),
    NUMBER(java.sql.Types.DOUBLE),
    VARCHAR(java.sql.Types.VARCHAR),
    DATE(java.sql.Types.TIMESTAMP),
    RECORD(-1),
    CURSOR(-1);

    fun toSqlValueNull(): Any {
        return when(this) {
            INT -> Long::class.javaObjectType

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

data class CursorData(val query: String, val params: List<ReturnResult> = emptyList(),
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

typealias Record = Array<Var>

private fun List<Var>.toSqlParams(): Array<Any?> = map { it.toSqlValue() }.toTypedArray()

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
            VarType.CURSOR -> (itValue as CursorData).toSqlValue(columnName)
            VarType.UNDEFINED -> throw Exception("undefined value for $name.$columnName")
        }
    }

    private fun getRecordValue(columnName: String?, value: Record): Any {

        val index = if(columnName == null) 0 else columnName.trim().toIntOrNull()

        return index?.let { value[it].toSqlValue() } ?:
        value.firstOrNull { it.name.equals(columnName, true) } ?:
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
        this.value = newVar.value
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

    private fun oper(params: List<VarResult>): VarResult = operations[oper]?.invoke(params, info) ?: throw Exception("operations for $oper not found")

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
        "NOT" to ::notFun,
        "AND" to ::andFun,
        "OR" to ::orFun//,
        //"NOW" to ::nowFun
)

//private fun nowFun(params: List<VarResult>): VarResult =

private fun outFun(params: List<VarResult>): VarResult = params[0].apply { this.value = VarType.UNDEFINED }

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