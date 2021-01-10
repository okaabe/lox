package me.otofune.void.front

sealed class Stmt {
    interface Visitor<T> {
        fun visitExprStmt(stmt: ExprStmt): T
        fun visitVarDeclStmt(stmt: VarDeclStmt): T
        fun visitPrintStmt(stmt: PrintStmt): T
        fun visitVarAssign(stmt: VarAssign): T
    }

    abstract fun <T> visit(visitor: Visitor<T>): T

    data class ExprStmt(val expr: Expr) : Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitExprStmt(this)
    }

    data class VarDeclStmt(
        val name: Token,
        val value: Expr
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitVarDeclStmt(this)
    }

    data class PrintStmt(
        val expr: Expr
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitPrintStmt(this)
    }

    data class VarAssign(
        val variable: Token,
        val newValue: Expr
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitVarAssign(this)
    }
}
