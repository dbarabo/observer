package ru.barabo.xls

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class Parser {
    private lateinit var vars: MutableList<Var>

    private val stackOper = Stack<ParseType>()

    private val stackPredikat = Stack<Any?>()

    private var filling: String = ""

    private var expression: String = ""

    fun parseExpression(expression: String, globalVars: MutableList<Var>): Expression {
        vars = globalVars

        stackOper.clear()
        stackPredikat.clear()

        this.expression = expression

        val expr = ArrayList<OperVar>()

        if(expression.isBlank()) return emptyList()

        var index = 0
        while(index < expression.length) {

            index = parseItem(expression[index].toUpperCase(), index)
        }

        return expr
    }

    private fun parseItem(item: Char, index: Int): Int {

        if(item.isWhitespace()) return parseBlank(index)

        filling = ""

        return when(item) {
            in 'A'..'Z', in 'А'..'Я' -> parseWord(index)
            in '0'..'9', '-' -> parseNumber(index)
            STRING_SEPARATOR -> parseString(index + 1)
            '[' -> openVar(index)
            ']' -> closeVar(index)
            '(' -> openBracket(index)
            ')' -> closeBracket(index)
            '=' -> parseOperEqual(index)
            '!' -> parseNotEqual(index)
            ';' -> parseEnd(index)
            else -> throw Exception("parseItem is not valid: $item index:$index exp:$expression")
           // '.'
        }
    }

    private fun parseEnd(index: Int): Int {

        if(stackOper.isNotEmpty()) throw Exception("';' stack Operations must be empty index:$index exp:$expression stack:$stackOper")

        if(stackPredikat.isNotEmpty()) {
            addSqlProcedure(stackPredikat.pop() as String)
        }

        if(stackPredikat.isNotEmpty()) {
            throw Exception("';' stack Predikats must be empty index:$index exp:$expression stack:$stackPredikat")
        }

        return index + 1;
    }

    private fun addSqlProcedure(procName: String) {
        OperVar(oper = Oper.SQL_EXEC, info = createCallSqlProcedure(procName) )
    }

    private fun createCallSqlProcedure(procName: String) = "{ call $procName; }"

    private fun parseNotEqual(index: Int): Int {
        val nextIndex = index + 1
        if(nextIndex >= expression.length) throw Exception("error is not end by symbol '!' index:$index exp:$expression")

        if(expression[nextIndex] == '=') {
            stackOper.push(ParseType.NOT_EQUAL)
            return nextIndex + 1
        } else {
            stackOper.push(ParseType.NOT)
            return nextIndex
        }
    }

    private fun parseOperEqual(index: Int): Int {
        val nextIndex = index + 1
        if(nextIndex >= expression.length) throw Exception("error is not end by symbol '=' index:$index exp:$expression")

        if(expression[nextIndex] == '=') {
            stackOper.push(ParseType.EQUAL)
            return nextIndex + 1
        } else {
            stackOper.push(ParseType.APPLY)
            return nextIndex
        }
    }

    private fun openVar(index: Int): Int {
        stackOper.push(ParseType.OPEN_VAR)
        return index + 1
    }

    private fun openBracket(index: Int): Int {
        stackOper.push(ParseType.OPEN_BRACKET)
        return index + 1
    }

    private fun closeVar(index: Int): Int {
        val oper = stackOper.pop()
        if(oper != ParseType.OPEN_VAR) throw Exception("stack operation wait '[' index:$index exp:$expression")
        return index + 1
    }

    private fun closeBracket(index: Int): Int {
        val oper = stackOper.pop()
        if(oper != ParseType.OPEN_BRACKET) throw Exception("stack operation wait '(' index:$index exp:$expression")
        return index + 1
    }

    private fun parseNumber(index: Int): Int =  parseAny(index, ::isNumber, ::numberFillerConverter)

    private fun numberFillerConverter(index: Int): Int {
        val value: Number? =
                if (filling.indexOf(DOUBLE_SEPARATOR) > 0) filling.toDoubleOrNull() else expression.toLongOrNull()

        if(value == null) throw Exception("expression is not number:$expression")

        stackPredikat.push(value)

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

    private fun parseString(index: Int): Int = parseAny(index, ::isString, ::stringFillerConverter) + 1

    private fun isString(item: Char): Boolean {
        return if(item != STRING_SEPARATOR) {
            filling += item
            true
        } else false
    }

    private fun parseWord(index: Int): Int = parseAny(index, ::isWord, ::wordCheck)

    private fun wordCheck(index: Int): Int {
        val oper = getOperByName(filling) ?: return predikatCheck(index)

        stackOper.push(oper)

        return index
    }

    private fun predikatCheck(index: Int): Int {

        if(stackOper.isEmpty() || ParseType.OPEN_VAR in stackOper || ParseType.OPEN_BRACKET in stackOper ) {
            stackPredikat.push(filling)
            return index
        }
/* TODO
        if(ParseType.APPLY in stackOper) {
            return parseCursor(index)
        }
*/

        return index
    }

    private fun getOperByName(operName: String): ParseType? = MAP_OPER_NAME[operName]

    private fun isWord(item: Char): Boolean {
        return when(item.toUpperCase()) {
            in 'A'..'Z', in 'А'..'Я', in '0'..'9', '_' -> {
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

    private fun stringFillerConverter(index: Int): Int {
        stackPredikat.push(filling)

        return index
    }
}

private enum class ParseType {
    OPEN_VAR,
    OPEN_BRACKET,
    APPLY,
    EQUAL,
    NOT_EQUAL,
    NOT,
    OR,
    AND
}

private val MAP_OPER_NAME = mapOf<String, ParseType>(
        "NOT" to ParseType.NOT,
        "OR" to ParseType.OR,
        "AND" to ParseType.AND )

private const val STRING_SEPARATOR = '\''

private const val DOUBLE_SEPARATOR = '.'