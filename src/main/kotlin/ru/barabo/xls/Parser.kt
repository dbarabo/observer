package ru.barabo.xls

import org.slf4j.LoggerFactory
import ru.barabo.db.Query
import ru.barabo.db.SessionSetting
import java.util.*
import kotlin.collections.ArrayList

typealias Expression = List<OperVar>

class Parser(private val query: Query) {
    private val logger = LoggerFactory.getLogger(Parser::class.java)

    private lateinit var vars: MutableList<Var>

    private val stackOper = Stack<ParseType>()

    private val stackPredikat = Stack<ReturnResult>()

    private val resultExp = ArrayList<OperVar>()

    private val sessionSetting: SessionSetting = query.uniqueSession()

    private var filling: String = ""

    private var expression: String = ""

    fun execExpression(expr: Expression, isRollBackAfterExec: Boolean = true): VarResult? {
        if(expr.isEmpty()) return null

        var result: VarResult? = null

        try {

            for (oper in expr) {
                result = oper.getVar()
            }

            if(isRollBackAfterExec) query.rollbackFree(sessionSetting)

        } catch (e: java.lang.Exception) {
            query.rollbackFree(sessionSetting)

            logger.error("execExpression", e)

            throw Exception(e.message)
        }
        return result
    }

    fun rollbackAfterExec() {
        query.rollbackFree(sessionSetting)
    }

    fun parseExpression(expression: String, globalVars: MutableList<Var>): Expression {
        vars = globalVars

        if(expression.isBlank()) return emptyList()

        stackOper.clear()
        stackPredikat.clear()
        resultExp.clear()

        this.expression = expression

        var index = 0
        while(index < expression.length) {

            index = parseItem(expression[index].toUpperCase(), index)
        }
        checkIsEndVarValue(expression)

        return resultExp.toList()
    }

    private fun checkIsEndVarValue(expression: String) {
        val trimExp = expression.trim()

        if(trimExp.lastIndex <= 0 || expression[trimExp.lastIndex] == ';') return

        var predikat: ReturnResult? = null
        while(stackPredikat.isNotEmpty()) {
            predikat = stackPredikat.pop()
        }

        if(predikat == null || predikat is OperVar) return

        resultExp += OperVar(oper = Oper.VAR, vars = listOf(predikat) )
    }

    private fun parseItem(item: Char, index: Int): Int {

        if(item.isWhitespace()) return parseBlank(index)

        filling = ""

        return when(item) {
            in 'A'..'Z', in 'А'..'Я' -> parseWord(index)
            in '0'..'9', '-' -> parseNumber(index)
            STRING_SEPARATOR -> parseString(index + 1)
            '[' -> openVar(index)
            '(' -> openBracket(index)
            ')' -> closeBracket(index)
            '=' -> parseOperEqual(index)
            '!' -> parseNotEqual(index)
            ';' -> parseEnd(index)
            else -> throw Exception("parseItem is not valid: $item index:$index exp:$expression")
        }
    }

    private fun parseEnd(index: Int): Int {

        if(stackOper.isNotEmpty()) throw Exception("';' stack Operations must be empty index:$index exp:$expression stack:$stackOper")

        var predikat: ReturnResult? = null

        while(stackPredikat.isNotEmpty()) {
            predikat = stackPredikat.pop()
        }

        if(predikat == null) return index + 1

        if(predikat !is OperVar && predikat.getVar().value is String) {
            val procName = predikat.getVar().value as String

            addSqlProcedure(procName)
        }

        return index + 1
    }

    private fun parseNotEqual(index: Int): Int {
        val nextIndex = index + 1
        if(nextIndex >= expression.length) throw Exception("error is not end by symbol '!' index:$index exp:$expression")

        return if(expression[nextIndex] == '=') {
            stackOper.push(ParseType.NOT_EQUAL)
            nextIndex + 1
        } else {
            stackOper.push(ParseType.NOT)
            nextIndex
        }
    }

    private fun parseOperEqual(index: Int): Int {
        val nextIndex = index + 1
        if(nextIndex >= expression.length) throw Exception("error is not end by symbol '=' index:$index exp:$expression")

        return if(expression[nextIndex] == '=') {
            stackOper.push(ParseType.EQUAL)
            nextIndex + 1
        } else {
            stackOper.push(ParseType.APPLY)
            modifyPredikatAsVar()
            nextIndex
        }
    }

    private fun modifyPredikatAsVar() {
        if(stackPredikat.isEmpty())  throw Exception("not found predikat from '=' operation")

        val varName = stackPredikat.pop().getVar().value as String

        val variable = findVar(varName)

        stackPredikat.push(variable.value)
    }

    private fun findVar(varName: String): Var {
        val varList = varName.split(VAR_SEPARATOR)

        val varMain = varList[0].trim().toUpperCase()

        val varAppender = if(varList.size > 1)varList[1].trim().toUpperCase() else ""

        val varFind = vars.firstOrNull { it.name == varMain }

        if(varAppender.isBlank()) {
            return varFind ?: createVar(varMain)
        }

        if(varFind?.value?.type != VarType.RECORD) throw Exception("appender $varAppender may be only VarType.RECORD")

        return (varFind.value.value as Record).columns.firstOrNull { it.name == varAppender } ?: throw Exception("column record not found:$varName")
    }

    private fun findMainVar(varName: String): Pair<Var, String>? {
        val varMain = varName.substringBefore(VAR_SEPARATOR).trim().toUpperCase()

        val varAppender = varName.substringAfter(VAR_SEPARATOR, "").trim().toUpperCase()

        val main = vars.firstOrNull { it.name == varMain }

        return main?.let { Pair(it, varAppender) }
    }

    private fun findVarResult(varName: String, isReturnNull: Boolean = false): ReturnResult? {
        val result = findMainVar(varName) ?: (if(isReturnNull) return null else throw Exception("var not found: $varName"))

        if(result.second.isBlank()) return result.first.value

        if(result.first.value.value !is CursorData) throw if(isReturnNull) return null else Exception("var must be cursor only: $varName")

        return (result.first.value.value as CursorData).getColumnResult(result.second)
    }

    private fun openVar(index: Int): Int {
        val end = expression.indexOf(CLOSE_VAR, index + 1)

        if(end < 0) throw Exception("for '[' symbol ']' not found index:$index exp:$expression")

        val varName = expression.substring((index + 1) until end).trim()

        val varResult = findVarResult(varName)!!

        tryExecOperByPredikat(varResult)

        return end + 1
    }


    private fun openBracket(index: Int): Int {
        if(stackOper.isEmpty() && stackPredikat.isNotEmpty()) {
            return parseSqlProcedure(index)
        }

        stackOper.push(ParseType.OPEN_BRACKET)
        return index + 1
    }

    private fun closeBracket(index: Int): Int {
        val oper = stackOper.pop()
        if(oper != ParseType.OPEN_BRACKET) {
            logger.error("oper=$oper")
            logger.error("stackOper=$stackOper")
            throw Exception("stack operation wait '(' index:$index exp:$expression")
        }
        tryExecOper()

        return index + 1
    }

    private fun parseNumber(index: Int): Int =  parseAny(index, ::isNumber, ::numberFillerConverter)

    private fun numberFillerConverter(index: Int): Int {
        val value: Number? =
                if (filling.indexOf(DOUBLE_SEPARATOR) > 0) filling.toDoubleOrNull() else filling.toLongOrNull()

        if(value == null) throw Exception("expression is not number:$filling")

        val varType = if(value is Double) VarType.NUMBER else VarType.INT

        tryExecOperByPredikat(VarResult(varType, value))

        return index
    }

    private fun isNumber(item: Char): Boolean {
        return when(item) {
            in '0'..'9', '-', DOUBLE_SEPARATOR -> {
                filling += item
                true
            }
            else -> false
        }
    }

    private fun parseString(index: Int): Int = parseAny(index, ::isString, ::constStringCheck) + 1

    private fun isString(item: Char): Boolean {
        return if(item != STRING_SEPARATOR) {
            filling += item
            true
        } else false
    }

    private fun parseWord(index: Int): Int = parseAny(index, ::isWord, ::wordCheck)

    private fun wordCheck(index: Int): Int {
        val oper = getOperByName(filling.toUpperCase()) ?: return predikatCheck(index)

        stackOper.push(oper)

        return index
    }

    private fun constStringCheck(index: Int): Int {

        tryExecOperByPredikat(VarResult(VarType.VARCHAR, filling))

        return index
    }

    private fun tryExecOper() {
        if(stackOper.isEmpty()) return

        val countParam = stackOper.peek().countParam

        if(countParam > stackPredikat.size) return

        val (oper, funInfo) = stackOper.pop().toOper()

        val params = Array<ReturnResult>(countParam) { VarResult.UNDEFINED }

        for(index in (countParam - 1) downTo 0) {
            params[index] = stackPredikat.pop()
        }

        val result = OperVar(oper = oper, vars = params.toList(), info = funInfo)

        stackPredikat.push(result)

        resultExp += result

        tryExecOper()
    }

    private fun tryExecOperByPredikat(newPredikat: ReturnResult) {
        stackPredikat.push(newPredikat)
        tryExecOper()
    }

    private fun predikatCheck(index: Int): Int {

        if(stackOper.isEmpty()) {
            stackPredikat.push(VarResult(VarType.VARCHAR, filling))
            return index
        }

        val varResult = findVarResult(filling, true) ?: return predikatCheckAsCursor(index)

        tryExecOperByPredikat(varResult)

        return index
    }

    private fun predikatCheckAsCursor(index: Int): Int {
        if(stackOper.peek() != ParseType.APPLY) throw Exception("predikat must be cursor only!!! index=$index exp:$expression")

        logger.error("stackOper=$stackOper")

        return parseCursor(index)
    }

    private fun parseSqlProcedure(index: Int): Int {
        val sqlProcName: String = stackPredikat.pop().getVar().value as String

        val (newIndex, queryText, params) = parseSql(sqlProcName, index, true)

        addSqlProcedure(queryText, params)

        return newIndex
    }

    private fun addSqlProcedure(sqlProcText: String, params: List<ReturnResult> = emptyList() ) {

        val firstParam = VarResult(VarType.SQL_PROC, value = QuerySession(query, sessionSetting))

        val paramProc = ArrayList<ReturnResult>()
        paramProc.add(firstParam)

        if(params.isNotEmpty()) {
            paramProc.addAll(params)
        }

        resultExp += OperVar(oper = Oper.SQL_EXEC, info = createCallSqlProcedure(sqlProcText), vars = paramProc)
    }

    private fun createCallSqlProcedure(procName: String) = "{ call $procName }"

    private fun parseCursor(index: Int): Int {
        logger.error("START parseCursor stackOper=$stackOper")

        val isSelect = filling.toUpperCase() == "SELECT"

        val (cursor, newIndex) = readCursorToEnd(index, isSelect)

        val varResult = VarResult(type = VarType.CURSOR, value = cursor)

        val variable = stackPredikat.pop()

        variable.setVar(varResult)

        logger.error("parseCursor stackOper=$stackOper")

        val oper = if(stackOper.isNotEmpty()) stackOper.pop() else throw Exception("stackOper is empty index:$index exp:$expression")

        if(oper != ParseType.APPLY) throw Exception("Oper must be ParseType.APPLY oper: $oper index:$index exp:$expression")

        return newIndex
    }

    private fun varWithOut(varOutNameUpTrim: String): ReturnResult {
        return if(varOutNameUpTrim.indexOf("OUT") == 0 &&
                varOutNameUpTrim.length >= 4 && varOutNameUpTrim[3].isWhitespace() ) {

            val varName = varOutNameUpTrim.substring(4).trim()

            val varResult = findVarResult(varName)!!

            val (oper, infoFun) = ParseType.OUT.toOper()

            OperVar(oper, infoFun, listOf(varResult))

        } else findVarResult(varOutNameUpTrim)!!
    }

    private fun parseSql(headerProc: String, index: Int, isCheckOutVar: Boolean = false): Triple<Int, String, List<ReturnResult>> {
        var selectText = headerProc

        var newIndex = index
        var indexStart = index

        val params = ArrayList<ReturnResult>()

        while(newIndex < expression.length && expression[newIndex] != END_COMMAND) {
            when( expression[newIndex] ) {
                OPEN_VAR -> {
                    if(newIndex > indexStart) {
                        selectText += expression.substring(indexStart until newIndex)
                    }
                    newIndex++
                    selectText += '?'

                    val end = expression.indexOf(CLOSE_VAR, newIndex)
                    val varName = expression.substring(newIndex until end).trim()

                    val returnResult = if(isCheckOutVar) varWithOut(varName.toUpperCase()) else findVarResult(varName)!!

                    params += returnResult

                    indexStart = end + 1
                    newIndex = indexStart
                }

                else -> newIndex++
            }
        }
        if(indexStart < expression.length && indexStart < newIndex) {
            selectText += expression.substring(indexStart until newIndex)
        }

        return Triple(newIndex, selectText, params)
    }

    private fun readCursorToEnd(index: Int, isSelect: Boolean): Pair<CursorData, Int> {
        val (newIndex, queryText, params) = parseSql(filling, index)

        val querySelect = if(isSelect) queryText else cursorFromQuery(queryText)

        val cursor = CursorData(QuerySession(query, sessionSetting), querySelect, params)

        return Pair(cursor, newIndex)
    }

    private fun cursorFromQuery(query: String) = "{ ? = call $query }"

    private fun getOperByName(operName: String): ParseType? = MAP_OPER_NAME[operName]

    private fun isWord(item: Char): Boolean {
        return when(item.toUpperCase()) {
            in 'A'..'Z', in 'А'..'Я', in '0'..'9', '_', '.' -> {
                filling += item
                true
            }
            else -> false
        }
    }

    private fun parseBlank(index: Int): Int = parseAny(index, { it.isWhitespace() }) { it }

    private fun parseAny(index: Int, isCondition: (Char)-> Boolean, fillerConverter: (index: Int) -> Int): Int {
        var newIndex = index
        while(newIndex < expression.length && isCondition(expression[newIndex])) newIndex++

        return fillerConverter(newIndex)
    }

    private fun createVar(varName: String): Var = Var(varName, VarResult()).apply { vars.add(this) }
}

private enum class ParseType(val countParam: Int) {
    OPEN_BRACKET(Int.MAX_VALUE),
    APPLY(2),
    EQUAL(2),
    NOT_EQUAL(2),
    NOT(1),
    OR(2),
    AND(2),
    OUT(1);

    fun toOper(): Pair<Oper, String> {
        return when(this) {
        APPLY -> Pair(Oper.APPLY, "")
        EQUAL -> Pair(Oper.FUN, "EQUAL")
        NOT_EQUAL -> Pair(Oper.FUN, "NOTEQUAL")
        NOT-> Pair(Oper.FUN, "NOT")
        OR -> Pair(Oper.FUN, "OR")
        AND -> Pair(Oper.FUN, "AND")
        OUT-> Pair(Oper.FUN, "OUT")
        else -> throw Exception("Oper not defined for $this")
        }
    }
}

private val MAP_OPER_NAME = mapOf(
        "NOT" to ParseType.NOT,
        "OR" to ParseType.OR,
        "AND" to ParseType.AND,
        "OUT" to ParseType.OUT)

private const val STRING_SEPARATOR = '\''

private const val DOUBLE_SEPARATOR = '.'

private const val VAR_SEPARATOR = '.'

private const val END_COMMAND = ';'

const val OPEN_VAR = '['
const val CLOSE_VAR = ']'