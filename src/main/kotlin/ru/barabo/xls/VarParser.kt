package ru.barabo.xls

import java.lang.Exception
import java.util.regex.Pattern

typealias Expression = List<OperVar>

class ExprParser {

    private lateinit var vars: MutableList<Var>

    fun parseExpression(expression: String, globalVars: MutableList<Var>): Expression {

        vars = globalVars

        val commands =  expression.split(END_COMMAND)
        if(commands.isEmpty()) return emptyList()

        val expr = ArrayList<OperVar>()

        for(command in commands) {
            if(command.trim().isBlank()) continue

            val first = command.indexOfFirst { it in listOf(APPLY, FUN, VAR)  }

            if(first == NOT_FOUND) throw Exception("not parsed expression:$command")

            expr += when(command[first]) {
                APPLY -> parseApply(command, first)
                //VAR ->  parseVar(command, first)
                //FUN -> parseFun(command, first)
                else -> throw Exception("!!!")
            }
        }

        return expr
    }

    private fun parseApply(command: String, eqIndex: Int): OperVar {

        val varApply = findOrCreateVar(command.substring(0, eqIndex).trim().toUpperCase())

        val expRight = command.substring(eqIndex + 1).trim()

        val (rightVar, info) = parseExpr(expRight)

        return OperVar(Oper.APPLY, info, listOf(varApply.value, rightVar))
    }

    private fun findOrCreateVar(varName: String): Var {
        if(varName.isBlank() ) throw Exception("var name is Blank")

        val varList = varName.split(VAR_SEPARATOR)

        val varMain = varList[0]

        val varAppender = if(varList.size > 1)varList[1].trim() else ""

        val varFind = vars.firstOrNull { it.name == varMain.trim() }

        if(varAppender.isBlank()) return varFind ?: createVar(varName)

        if(varFind?.value?.type != VarType.RECORD) throw Exception("appender $varAppender may be only VarType.RECORD")

        return (varFind.value.value as Record).columns.firstOrNull { it.name == varAppender } ?: throw Exception("column record not found:$varName")
    }

    private fun findVar(varFullName: String): Var? {
        val varList = varFullName.split(VAR_SEPARATOR)

        val varMain = varList[0].trim()

        val varAppender = if(varList.size > 1) varList[1].trim() else ""

        val varFind = vars.firstOrNull { it.name == varMain } ?: return null

        if(varAppender.isBlank() ) return varFind
/*
        return when(varFind.value.type)
        VarType.RECORD ->



        if(varFind?.value?.type !in listOf(VarType.RECORD, VarType.CURSOR) ) throw Exception("$varFullName may be only RECORD or CURSOR")

*/
        return null
    }

    private fun parseExpr(expression: String): VarExpr {
        return when {
            isSelect(expression) -> createSelectVar(expression)
            isConst(expression) -> createConst(expression)
            isVar(expression) -> byVar(expression)
            //isCursorCall(expression) -> createCursorAnyVar(expression)
            //isFun(expression) -> byFunExpr(expression)
            else -> throw Exception("not parse expr:$expression")
        }
    }

    private fun byVar(expression: String): VarExpr {
        TODO()
    }

    private fun createConst(expression: String): VarExpr {

        if(expression[0] == CHAR_TYPE) {
            return VarExpr(VarResult(VarType.VARCHAR, getVarchar(expression)), "")
        }
        val number = getNumber(expression)

        val type = if(number is Double) VarType.NUMBER else VarType.INT

        return VarExpr(VarResult(type, number), "")
    }

    private fun getNumber(expression: String): Number {
        val value: Number? = if (expression.indexOf(DOUBLE_SEPARATOR) > 0) expression.toDoubleOrNull()
                          else expression.toLongOrNull()

        return value ?: throw Exception("expression is not number:$expression")
    }

    private fun getVarchar(expression: String): String {
        val pos = expression.substring(1).indexOf(CHAR_TYPE)

        return if(pos >= 0) expression.substring(1, pos + 1) else expression.substring(1)
    }

    private fun createSelectVar(expression: String): VarExpr {

        val selectCursor = complexValue(expression)

        val cursor = CursorData(selectCursor.info, selectCursor.vars)

        return VarExpr(VarResult(VarType.CURSOR, cursor), "")
    }

    private fun complexValue(expr: String): SqlVarsInfo {
        var newValue = expr

        val matcher = Pattern.compile("\\[(.*?)\\]").matcher(newValue)

        val varsList = ArrayList<ReturnResult>()

        while (matcher.find()) {

            val varName = matcher.group(1)

            val varFind = vars.firstOrNull { it.name == varName.trim().toUpperCase() } ?: throw Exception ("var not found: $varName")

            varsList.add(varFind.value)

            newValue = newValue.replace("\\[$varName\\]".toRegex(), "?")
        }

        return SqlVarsInfo(varsList, newValue)
    }

   //  private fun getVarByName()

    private fun isVar(expression: String): Boolean = expression.isNotEmpty() && expression[0] == VAR

    private fun isConst(expression: String): Boolean {
        return  expression.isNotEmpty() &&
                (expression[0] == CHAR_TYPE || expression[0].isDigit() || expression[0] == MINUS)
    }

    private fun isSelect(expression: String): Boolean = expression.toUpperCase().indexOf("SELECT ") == 0

    private fun createVar(varName: String): Var = Var(varName, VarResult.UNDEFINED).apply { vars.add(this) }
}

private data class VarExpr(val vars: ReturnResult, val info: String)

private data class SqlVarsInfo(val vars: List<ReturnResult>, val info: String)

private const val MINUS = '-'

private const val VAR_SEPARATOR = '.'

private const val DOUBLE_SEPARATOR = '.'

private const val CHAR_TYPE = '\''

private const val END_COMMAND = ';'

private const val APPLY = '='

private const val FUN = '('

private const val VAR = '['

private const val NOT_FOUND = -1


