package me.otofune.void.front

sealed class Expr {
    interface Visitor<T> {
        fun acceptBinary(expr: Binary) : T
        fun acceptGrouping(expr: Grouping): T
        fun acceptUnary(expr: Unary): T
        fun acceptLiteral(expr: Literal): T
    }

    abstract fun <T> accept(visitor: Visitor<T>): T

    data class Binary(
        val left: Expr,
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptBinary(this)
    }

    data class Unary(
        val op: Token,
        val right: Expr
    ) : Expr() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptUnary(this)
    }

    data class Literal(val value: Any?): Expr() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptLiteral(this)
    }

    data class Grouping(val expr: Expr) : Expr() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptGrouping(this)
    }
}
