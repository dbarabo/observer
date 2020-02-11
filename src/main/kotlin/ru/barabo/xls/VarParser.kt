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

        return vars.firstOrNull { it.name == varName } ?: createVar(varName)
    }

    private fun parseExpr(expression: String): VarExpr {
        return when {
            isSelect(expression) -> createSelectVar(expression)
            //isVar(expression) -> byVar(expression)
            //isCursorCall(expression) -> createCursorAnyVar(expression)
            //isFun(expression) -> byFunExpr(expression)
            else -> throw Exception("not parse expr:$expression")
        }
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

    private fun isSelect(expression: String): Boolean = expression.toUpperCase().indexOf("SELECT ") == 0

    private fun createVar(varName: String): Var = Var(varName, VarResult.UNDEFINED).apply { vars.add(this) }
}

private data class VarExpr(val vars: ReturnResult, val info: String)

private data class SqlVarsInfo(val vars: List<ReturnResult>, val info: String)

private const val END_COMMAND = ';'

private const val APPLY = '='

private const val FUN = '('

private const val VAR = '['

private const val NOT_FOUND = -1


