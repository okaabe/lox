package me.otofune.void.grammar

sealed class Stmt {
    interface Visitor<T> {
        fun visitStmt(stmt: Stmt): T = stmt.visit(this)

        fun visitBlockStmt(stmt: BlockStmt): T
        fun visitExprStmt(stmt: ExprStmt): T
        fun visitVarDeclStmt(stmt: VarDeclStmt): T
        fun visitPrintStmt(stmt: PrintStmt): T
        fun visitIfStmt(stmt: IfStmt): T
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

    data class IfStmt(
        val condition: Expr,
        val thenDo: Stmt,
        val elseDo: Stmt? = null
    ) : Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitIfStmt(this)
    }

    data class BlockStmt(
        val statements: List<Stmt>
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitBlockStmt(this)
    }
}
