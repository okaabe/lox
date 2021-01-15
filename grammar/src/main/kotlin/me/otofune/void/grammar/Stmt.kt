package me.otofune.void.grammar

sealed class Stmt {
    interface Visitor<T> {
        fun visitStmt(stmt: Stmt): T = stmt.visit(this)

        fun visitBlockStmt(stmt: BlockStmt): T
        fun visitExprStmt(stmt: ExprStmt): T
        fun visitVarStmt(stmt: VarStmt): T
        fun visitIfStmt(stmt: IfStmt): T
        fun visitFunctionStmt(stmt: FunctionStmt): T
        fun visitReturnStmt(stmt: ReturnStmt): T
        fun visitWhileStmt(stmt: WhileStmt): T
    }

    abstract fun <T> visit(visitor: Visitor<T>): T

    data class ExprStmt(val expr: Expr) : Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitExprStmt(this)
    }

    data class FunctionStmt(
        val name: Token,
        val params: List<Token>,
        val body: List<Stmt>
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitFunctionStmt(this)
    }

    data class VarStmt(
        val name: Token,
        val value: Expr
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitVarStmt(this)
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

    data class ReturnStmt(
        val expression: Expr?
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitReturnStmt(this)
    }

    data class WhileStmt(
        val condition: Expr,
        val body: Stmt
    ): Stmt() {
        override fun <T> visit(visitor: Visitor<T>): T = visitor.visitWhileStmt(this)
    }
}
