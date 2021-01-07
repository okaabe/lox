package me.otofune.void.front

sealed class Stmt {
    interface Visitor<T> {
        fun acceptExpr(stmt: ExprStmt): T
        fun acceptVarDecl(stmt: VarDeclStmt): T
    }

    abstract fun <T> accept(visitor: Visitor<T>): T

    data class ExprStmt(val expr: Expr) : Stmt() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptExpr(this)
    }

    data class VarDeclStmt(
        val name: Token,
        val value: Expr
    ): Stmt() {
        override fun <T> accept(visitor: Visitor<T>): T = visitor.acceptVarDecl(this)
    }
}
