package me.otofune.void.interpreter.runtime

import me.otofune.void.front.Expr
import me.otofune.void.front.Stmt

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
    }

    override fun visitPrintStmt(stmt: Stmt.PrintStmt) {
        println(evaluator.visitExpr(stmt.expr))
    }

    override fun visitVarAssign(stmt: Stmt.VarAssign) {
        environment.assign(stmt.variable.lexeme, stmt.newValue)
    }
}