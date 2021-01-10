package me.otofune.void.interpreter

import me.otofune.void.front.*

class Interpreter : Expr.Visitor<Any?>, Stmt.Visitor<Any?> {
    private val callframe = Callframe()

    private fun evaluate(expr: Expr): Any? = expr.visit(this)

    fun interpret(stmts: List<Stmt>) {
        stmts.forEach { stmt ->
            stmt.visit(this)
        }
    }

    override fun visitVarAssign(stmt: Stmt.VarAssign) {
    }

    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        return callframe.getAndCheckExistence(expr.variable.lexeme)
    }

    override fun visitVarDeclStmt(stmt: Stmt.VarDeclStmt) {
        callframe.declareVar(stmt.name.lexeme, evaluate(stmt.value))
    }

    override fun visitPrintStmt(stmt: Stmt.PrintStmt) {
        println(evaluate(stmt.expr))
    }

    override fun visitExprStmt(stmt: Stmt.ExprStmt): Any? {
        return evaluate(stmt.expr)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> left.toString().toDouble() - right.toString().toDouble()
            TokenType.SLASH -> left.toString().toDouble() / right.toString().toDouble()
            TokenType.STAR -> left.toString().toDouble() * right.toString().toDouble()
            TokenType.LESS -> left.toString().toDouble() < right.toString().toDouble()
            TokenType.LESS_EQUAL -> left.toString().toDouble() <= right.toString().toDouble()
            TokenType.GREATER -> left.toString().toDouble() > right.toString().toDouble()
            TokenType.GREATER_EQUAL -> left.toString().toDouble() >= right.toString().toDouble()
            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            TokenType.BANG_EQUAL -> !isEqual(left, right)

            else -> null
        }
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> -(right.toString().toDouble())
            TokenType.BANG -> !isTruthy(right)
            else -> {
                return null
            }
        }
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        return when {
            a == null && b == null -> true
            a == null -> false

            else -> a.equals(b)
        }
    }

    private fun isTruthy(value: Any?): Boolean {
        if (value == null) return false
        println("Is boolean ${value is Boolean}")
        if (value is Boolean) return value.toString().toBoolean()

        return true
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? = evaluate(expr.expr)

    override fun visitLiteralExpr(expr: Expr.Literal): Any? = expr.value
}
