package me.otofune.void.front

sealed class Expr {
    interface Visitor<T> {
        fun visitExpr(expr: Expr): T = expr.visit(this)
        fun visitBinaryExpr(expr: Binary) : T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitUnaryExpr(expr: Unary): T
        fun visitLiteralExpr(expr: Literal): T
        fun visitVariableExpr(expr: Variable): T
    }

    abstract fun <T> visit(visitor: Visitor<T>): T

    data class Binary(
        val left: Expr,
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitBinaryExpr(this)
    }

    data class Unary(
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitUnaryExpr(this)
    }

    data class Literal(val value: Any?): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitLiteralExpr(this)
    }

    data class Grouping(val expr: Expr) : Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitGroupingExpr(this)
    }

    data class Variable(val variable: Token): Expr() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitVariableExpr(this)
    }
}
