package me.otofune.void.interpreter.runtime

import me.otofune.void.grammar.Stmt
import me.otofune.void.interpreter.runtime.util.isTruthy

class Executor(
    private val evaluator: Evaluator,
    private var environment: Environment
) : Stmt.Visitor<Any?> {

    override fun visitExprStmt(stmt: Stmt.ExprStmt): Any? {
        return evaluator.visitExprWithScope(stmt.expr, environment)
    }

    override fun visitVarStmt(stmt: Stmt.VarStmt) {
        environment.declare(stmt.name.lexeme, evaluator.visitExprWithScope(stmt.value, environment))
    }

    override fun visitIfStmt(stmt: Stmt.IfStmt) {
        if (isTruthy(evaluator.visitExprWithScope(stmt.condition, environment))) {
            visitStmt(stmt.thenDo)
        } else stmt.elseDo?.also { visitStmt(it) }
    }

    override fun visitBlockStmt(stmt: Stmt.BlockStmt) {
        val globalScope = environment

        try {
            environment = Environment(globalScope)

            for (statement in stmt.statements) visitStmt(statement)
        } finally {
            environment = globalScope
        }
    }

    override fun visitPrintStmt(stmt: Stmt.PrintStmt) {
        println(evaluator.visitExprWithScope(stmt.expr, environment))
    }

    override fun visitCallStmt(stmt: Stmt.CallStmt): Any? {
        TODO("Not yet implemented")
    }

    override fun visitFunctionStmt(stmt: Stmt.FunctionStmt): Any? {
        TODO("Not yet implemented")
    }
}
