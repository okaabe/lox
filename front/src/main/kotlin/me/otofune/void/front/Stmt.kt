package me.otofune.void.front

sealed class Stmt {
    interface Visitor<T> {
        fun acceptExpr(expr: ExprStmt): T
    }

    abstract fun <T> accept(visitor: Visitor<T>): T

    data class ExprStmt(val expr: Expr) : Stmt() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptExpr(this)
    }
}
