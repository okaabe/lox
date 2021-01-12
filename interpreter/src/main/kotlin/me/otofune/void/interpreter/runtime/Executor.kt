package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Expr
import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.runtime.util.isTruthy

class Executor(
    private val evaluator: Expr.Visitor<Any?>,
    private val environment: Environment
) : Stmt.Visitor<Any?> {

    override fun visitExprStmt(stmt: Stmt.ExprStmt): Any? {
        return evaluator.visitExpr(stmt.expr)
    }

    override fun visitVarDeclStmt(stmt: Stmt.VarDeclStmt) {
        environment.declare(stmt.name.lexeme, evaluator.visitExpr(stmt.value))
    }

    override fun visitIfStmt(stmt: Stmt.IfStmt) {
        if (isTruthy(evaluator.visitExpr(stmt.condition))) {
            visitStmt(stmt.thenDo)
        } else stmt.elseDo?.also { visitStmt(it) }
    }

    override fun visitBlockStmt(stmt: Stmt.BlockStmt) = stmt.statements.map { visitStmt(it) }

    override fun visitPrintStmt(stmt: Stmt.PrintStmt) {
        println(evaluator.visitExpr(stmt.expr))
    }
}
