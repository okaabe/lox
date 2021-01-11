package me.otofune.void.interpreter.runtime

import me.otofune.void.front.Expr
import me.otofune.void.front.TokenType

import me.otofune.void.interpreter.runtime.util.checkNumberOperand
import me.otofune.void.interpreter.runtime.util.isEqual
import me.otofune.void.interpreter.runtime.util.isTruthy

class Evaluator(
    private val environment: Environment
): Expr.Visitor<Any?> {
    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val left = visitExpr(expr.left)
        val right = visitExpr(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> checkNumberOperand(expr.op, left) - checkNumberOperand(expr.op, right)
            TokenType.STAR -> checkNumberOperand(expr.op, left) * checkNumberOperand(expr.op, right)
            TokenType.SLASH -> checkNumberOperand(expr.op, left) / checkNumberOperand(expr.op, right)
            TokenType.PLUS -> when {
                left is String && right is String -> left + right
                else -> checkNumberOperand(expr.op, left) + checkNumberOperand(expr.op, right)
            }
            TokenType.LESS -> checkNumberOperand(expr.op, left) < checkNumberOperand(expr.op, right)
            TokenType.GREATER -> checkNumberOperand(expr.op, left) > checkNumberOperand(expr.op, right)
            TokenType.LESS_EQUAL -> checkNumberOperand(expr.op, left) <= checkNumberOperand(expr.op, right)
            TokenType.GREATER_EQUAL -> checkNumberOperand(expr.op, left) >= checkNumberOperand(expr.op, right)

            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            TokenType.BANG_EQUAL -> !isEqual(left, right)

            else -> null
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? = visitExpr(expr.expr)

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = visitExpr(expr.right)

        return when(expr.op.type) {
            TokenType.MINUS -> -checkNumberOperand(expr.op, right)
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? = expr.value

    override fun visitVariableExpr(expr: Expr.Variable): Any? = environment.get(expr.variable.lexeme)
}